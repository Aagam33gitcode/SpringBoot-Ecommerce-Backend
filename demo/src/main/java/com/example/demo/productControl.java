package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class productControl {

    private productRepo repository;
    productControl(productRepo repository){
        this.repository=repository;
    }
    @GetMapping
    public String hello(){
        return "Hello";
    }
    @GetMapping("detail")
    public ResponseEntity<List<products>> getProduct(){

        return new ResponseEntity<>( repository.findAll(), HttpStatus.OK);

    }
    @GetMapping("detail/{id}")
    public ResponseEntity<Optional<products>> getByid(@PathVariable int id ){
        if(repository.findById(id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(repository.findById(id), HttpStatus.OK);
    }

    @GetMapping("detail/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable int id){
        return new ResponseEntity<>(repository.getReferenceById(id).getImageData(),HttpStatus.OK);
    }




    @PostMapping("add")
    public ResponseEntity<products> addpro(products pro){
        return new ResponseEntity<>(repository.save(pro), HttpStatus.ACCEPTED);
    }

    @PostMapping("addimage")
    public  ResponseEntity<?> adddetail(@RequestPart products pro, @RequestPart MultipartFile image)   {

//       products prd=service.addProduct(pro,image);

        try {
            pro.setImageName(image.getOriginalFilename());
            pro.setImageType(image.getContentType());
            pro.setImageData(image.getBytes());
            return new ResponseEntity<>( repository.save(pro),HttpStatus.CREATED) ;

        } catch (IOException e) {
            return  new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR) ;

        }
    }

}
