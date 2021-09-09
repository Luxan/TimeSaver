package com.sgorokh.TimeSaver.domain.servises;

import com.sgorokh.TimeSaver.domain.dtos.ClientDTO;
import com.sgorokh.TimeSaver.domain.dtos.ClientDetailsDTO;
import com.sgorokh.TimeSaver.domain.helpers.DtoToEntityMapper;
import com.sgorokh.TimeSaver.domain.helpers.EntityToDtoMapper;
import com.sgorokh.TimeSaver.models.Client;
import com.sgorokh.TimeSaver.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public List<ClientDTO> getClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(EntityToDtoMapper::clientToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ClientDetailsDTO> getClientDetailsById(Long clientId) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            return Optional.of(EntityToDtoMapper.clientToClientDetailsDto(client));
        } else return Optional.empty();
    }

    @Transactional(readOnly = true)
    public List<ClientDTO> searchByString(String searchString) {
        List<Client> clients = clientRepository.searchByString(searchString);
        return clients.stream()
                .map(EntityToDtoMapper::clientToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClientDTO saveClient(ClientDTO clientDto) {
        Client foundClient = clientRepository.searchByName(clientDto.getName());
        if (foundClient != null) {
            if (foundClient.getEmail().equals(clientDto.getEmail()) ||
                    foundClient.getPhone().equals(clientDto.getPhone()))
                return null;
        }
        Client client = DtoToEntityMapper.clientDtoToClient(clientDto);
        Client savedClient = clientRepository.save(client);
        return EntityToDtoMapper.clientToDto(savedClient);
    }

    @Transactional
    public ClientDTO updateClient(ClientDTO clientDto) {
        Optional<Client> foundClient = clientRepository.findById(clientDto.getId());
        if (!foundClient.isPresent()) return null;
        Client client = foundClient.get();
        client.setName(clientDto.getName());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        Client savedClient = clientRepository.save(client);
        return EntityToDtoMapper.clientToDto(savedClient);
    }

    @Transactional
    public ClientDTO deleteClient(Long clientId) {
        Optional<Client> foundClient = clientRepository.findById(clientId);
        if (!foundClient.isPresent()) return null;
        Client client = foundClient.get();
        clientRepository.delete(client);
        return EntityToDtoMapper.clientToDto(client);
    }
}
