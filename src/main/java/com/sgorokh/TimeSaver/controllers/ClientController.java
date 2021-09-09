package com.sgorokh.TimeSaver.controllers;

import com.sgorokh.TimeSaver.controllers.exceptions.InvalidRequestException;
import com.sgorokh.TimeSaver.controllers.exceptions.ResourceNotFoundException;
import com.sgorokh.TimeSaver.controllers.helpers.ObjectMapper;
import com.sgorokh.TimeSaver.controllers.helpers.RequestToDTOMapper;
import com.sgorokh.TimeSaver.controllers.requests.CreateClientRequest;
import com.sgorokh.TimeSaver.controllers.requests.UpdateClientRequest;
import com.sgorokh.TimeSaver.controllers.responses.*;
import com.sgorokh.TimeSaver.domain.dtos.ClientDTO;
import com.sgorokh.TimeSaver.domain.dtos.ClientDetailsDTO;
import com.sgorokh.TimeSaver.domain.servises.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.path}")
public class ClientController {

    @Value("${api.path}")
    private String apiPath;

    private final ClientService clientService;

    @Autowired
    ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("clients")
    public ResponseEntity<MultipleClientResponse> getClients() {
        List<ClientDTO> clients = clientService.getClients();
        MultipleClientResponse response = ObjectMapper.clientToMultipleClientDetailsResponse(clients);
        return ResponseEntity.ok(response);
    }

    @GetMapping("clients/{id}")
    public ResponseEntity<ClientDetailsResponse> getClientDetails(@PathVariable(value = "id") Long clientId)
            throws ResourceNotFoundException {
        Optional<ClientDetailsDTO> clientById = clientService.getClientDetailsById(clientId);
        ClientDetailsDTO client = clientById.orElseThrow(() ->
                new ResourceNotFoundException("Could not get Client by id = " + clientId));
        ClientDetailsResponse response = ObjectMapper.clientDetailsDtoToClientDetailsResponse(client);
        return ResponseEntity.ok(response);
    }

    @GetMapping("clients/search/{searchString}")
    public ResponseEntity<MultipleClientResponse> getClientsByString(@PathVariable(value = "searchString") String searchString) {
        List<ClientDTO> clientList = clientService.searchByString(searchString);
        MultipleClientResponse response = ObjectMapper.clientToMultipleClientDetailsResponse(clientList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("clients")
    public ResponseEntity<NameResponse> createClient(@RequestBody CreateClientRequest clientRequest,
                                                     WebRequest request) throws URISyntaxException, InvalidRequestException {
        ClientDTO client = RequestToDTOMapper.createClientRequestToClientDto(clientRequest);
        ClientDTO savedClient = clientService.saveClient(client);
        if (savedClient == null) {
            throw new InvalidRequestException("Client with this phone number or name or email already exist: "
                    + client.getName() + " " + client.getPhone() + " " + client.getEmail());
        }
        URI uri = new URI(request.getContextPath() + apiPath + "clients");
        NameResponse response = ObjectMapper.nameToNameResponse(savedClient.getName());
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("clients")
    public ResponseEntity<NameResponse> updateClient(@Valid @RequestBody UpdateClientRequest request)
            throws InvalidRequestException {
        ClientDTO client = RequestToDTOMapper.updateClientRequestToClientDto(request);
        ClientDTO updatedClient = clientService.updateClient(client);
        if (updatedClient == null)
            throw new InvalidRequestException("Client with this id does not exist" + client.getId());

        NameResponse response = ObjectMapper.nameToNameResponse(client.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("clients/{id}")
    public ResponseEntity<NameResponse> deleteClient(@PathVariable(value = "id") Long clientId)
            throws InvalidRequestException {
        ClientDTO deletedClient = clientService.deleteClient(clientId);
        if (deletedClient == null)
            throw new InvalidRequestException("Client with this id does not exist: " + clientId);

        NameResponse response = ObjectMapper.nameToNameResponse(deletedClient.getName());
        return ResponseEntity.ok(response);
    }
}
