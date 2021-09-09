package com.sgorokh.TimeSaver.domain.servises;

import com.sgorokh.TimeSaver.domain.dtos.ImageDTO;
import com.sgorokh.TimeSaver.domain.dtos.SessionDTO;
import com.sgorokh.TimeSaver.domain.helpers.EntityToDtoMapper;
import com.sgorokh.TimeSaver.models.Image;
import com.sgorokh.TimeSaver.models.Session;
import com.sgorokh.TimeSaver.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    @Autowired
    SessionService(
            SessionRepository sessionRepository
    ) {
        this.sessionRepository = sessionRepository;
    }

    public Optional<SessionDTO> getSmallImageSessionDtoBySessionId(Long sessionId) {
        return getSessionDTO(sessionId, Image::getSmallImage);
    }

    public Optional<SessionDTO> getOriginalImageSessionDtoBySessionId(Long sessionId) {
        return getSessionDTO(sessionId, Image::getOriginalImage);
    }

    private Optional<SessionDTO> getSessionDTO(Long sessionId, Function<Image, Byte[]> function) {
        Optional<Session> optionalSession = sessionRepository.findById(sessionId);
        if (optionalSession.isPresent()) {
            Session session = optionalSession.get();
            List<Image> images = session.getImages();
            List<ImageDTO> dtos = images.stream()
                    .map(image -> EntityToDtoMapper.imageToDTO(image, function))
                    .collect(Collectors.toList());
            return Optional.of(new SessionDTO(session.getName(), dtos));
        } else return Optional.empty();
    }

}
