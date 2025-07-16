package com.example.market.kafka;


import com.example.market.dto.request.BookRequest;
import com.example.market.dto.request.ImageEvent;
import com.example.market.dto.response.BookResponse;
import com.example.market.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final BookService bookService;
    @Transactional
    @KafkaListener(topics = "save-image", groupId = "image-group")
    public void listenSaveImageEvent(ImageEvent imageEvent) throws IOException {
        log.info("Received save image event: {}", imageEvent);

        String imageId = imageEvent.getId();
        BookResponse bookResponse = bookService.findByImageId(imageId);
        if (bookResponse != null) {
            BookRequest bookRequest = new BookRequest();
            bookRequest.setImageId(imageId);
            bookService.updateBook(bookResponse.getId(), bookRequest);
            log.info("Updated book with image ID: {}", imageId);
        } else {
            log.warn("No book found for image ID: {}", imageId);
        }
    }
}
