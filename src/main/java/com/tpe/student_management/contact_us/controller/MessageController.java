package com.tpe.student_management.contact_us.controller;

import com.tpe.student_management.contact_us.dto.MessageRequestDTO;
import com.tpe.student_management.contact_us.dto.MessageResponseDTO;
import com.tpe.student_management.contact_us.entity.Message;
import com.tpe.student_management.contact_us.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/save") //http://localhost:8080/contact/save + POST + BODY
    public ResponseEntity<Map<String, ?>> saveContactMessage(@RequestBody @Valid MessageRequestDTO dto){
        return new ResponseEntity<>(messageService.saveContactMessage(dto), HttpStatus.CREATED);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Page<MessageResponseDTO>> findAllWithPagination(@RequestParam(defaultValue = "1") int page,
                                                                          @RequestParam(defaultValue = "25") int size,
                                                                          @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                          @RequestParam(defaultValue = "ASC") Sort.Direction order){
        return ResponseEntity.ok(messageService.findAllWithPagination(page, size, sortBy, order));
    }

    @GetMapping("/search-by-email")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Page<MessageResponseDTO>> findAllByEmailWithPagination(@RequestParam(defaultValue = "1") int page,
                                                                                 @RequestParam(defaultValue = "25") int size,
                                                                                 @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                                 @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                                                 @RequestParam String email
                                                                                 ){
        return ResponseEntity.ok(messageService.findAllByEmail(page, size, sortBy, order, email));
    }

    //! Additional endpoints

    //********* 1 - searchBySubject()
    @GetMapping("/searchBySubject") //http://localhost:8080/contactMessages/searchBySubject?subject=deneme
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<MessageResponseDTO> searchBySubject(
            @RequestParam(value = "subject") String subject,
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(value = "size",defaultValue = "10") int size,
            @RequestParam(value = "sort",defaultValue = "createdAt") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type){
        return messageService.searchBySubject(subject,page,size,sort,type);
    }

    //********* 2 - searchByDateBetween()
    @GetMapping("/searchBetweenDates") //http://localhost:8080/contactMessages/searchBetweenDates?beginDate=2023-09-13&endDate=2023-09-15
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<Message>> searchByDateBetween(
            @RequestParam(value = "beginDate") String beginDateString,
            @RequestParam(value = "endDate") String endDateString){
        //! Parameters could also be LocalDateTime type. ConversionService supports this.
        //! If format is not changed, default is: yyyy-MM-ddTHH:mm:ss
        List<Message> contactMessages = messageService.searchByDateBetween(beginDateString, endDateString);
        return ResponseEntity.ok(contactMessages);
    }

    //********* 3 - deleteById() PathVariable
    @DeleteMapping("/deleteById/{contactMessageId}")//http://localhost:8080/contactMessages/deleteById/2
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<String> deleteById(@PathVariable Long contactMessageId){
        return ResponseEntity.ok(messageService.deleteById(contactMessageId));
    }

    //********* 4 - deleteByIdUsingQueryParameter() Opsiyonel
    @DeleteMapping("/deleteByIdParam") //http://localhost:8080/contactMessages/deleteByIdParam?contactMessageId=1
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<String> deleteByIdUsingQueryParameter(@RequestParam(value = "contactMessageId")
                                                                Long contactMessageId){
        return ResponseEntity.ok(messageService.deleteById(contactMessageId));
    }

    //********* 5 - findById()
    @GetMapping("/getById/{contactMessageId}")//http://localhost:8080/contactMessages/getById/1
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Message> findById(@PathVariable Long contactMessageId){
        return ResponseEntity.ok(messageService.findById(contactMessageId));
    }

    //********* 6 - findByIdUsingQueryParameter() Opsiyonel
    @GetMapping("/getByIdParam") //http:/getConftactMessageById/localhost:8080/contactMessages/getByIdParam?contactMessageId=1
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Message> findByIdUsingQueryParameter(@RequestParam(value = "contactMessageId")
                                                                      Long contactMessageId){
        return ResponseEntity.ok(messageService.findById(contactMessageId));
    }
}
