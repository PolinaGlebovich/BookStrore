package com.example.market.kafka;

import com.example.market.entity.Book;
import com.example.market.service.BookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class KafkaMessagingService {

    private final BookService bookService;

    @Transactional
    @KafkaListener(topics = "${spring.kafka.topic.save-image}", groupId = "image-group")
    public void handleSaveImageEvent(ImageEvent imageEvent) throws IOException {
        log.info("Received save image event: {}", imageEvent);

        String imageId = imageEvent.getId();
        Book book = bookService.findByImageId(imageId);
        if (book != null) {
            book.setImageId(imageId);
            bookService.save(book, null);
            log.info("Save book with image ID: {}", imageId);
        } else {
            log.warn("No book found for image ID: {}", imageId);
        }
    }

    @Transactional
    @KafkaListener(topics = "${spring.kafka.topic.delete-image}", groupId = "image-group")
    public void handleDeleteImageEvent(ImageEvent imageEvent) throws IOException {
        log.info("Received delete image event: {}", imageEvent);

        String imageId = imageEvent.getId();
        Book book = bookService.findByImageId(imageId);
        if (book != null) {
            book.setImageId(null);
            bookService.save(book, null);
            log.info("Removed image ID from book: {}", imageId);
        } else {
            log.warn("No book found for image ID: {}", imageId);
        }
    }

    @Transactional
    @KafkaListener(topics = "${spring.kafka.topic.download-image}", groupId = "image-group")
    public void handleDownloadImageEvent(ImageEvent imageEvent) {
        log.info("Received download image event: {}", imageEvent);

        String imageId = imageEvent.getId();
        Book book = bookService.findByImageId(imageId);
        if (book != null) {
            log.info("Image downloaded for book: {}", book.getTitle());
        } else {
            log.warn("No book found for image ID: {}", imageId);
        }
    }


    @Transactional
    @KafkaListener(topics = "${spring.kafka.topic.viewAll-image}", groupId = "image-group")
    public void handleViewAllImagesEvent(List<ImageEvent> imageEvents) {
        log.info("Received view all images event: {}", imageEvents);

        for (ImageEvent imageEvent : imageEvents) {
            String imageId = imageEvent.getId();
            Book book = bookService.findByImageId(imageId);
            if (book != null) {
                log.info("Image viewed for book: {}", book.getTitle());
            } else {
                log.warn("No book found for image ID: {}", imageId);
            }
        }
    }
}