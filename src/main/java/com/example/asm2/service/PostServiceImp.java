package com.example.asm2.service;

import com.example.asm2.dao.PostRepository;
import com.example.asm2.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PostServiceImp implements PostService{

private PostRepository postRep;


    @Autowired
    public PostServiceImp(PostRepository thePostRepository ){
        postRep = thePostRepository;

    }
    @Override
    public List<Post> findAll() {
        return postRep.findAll();
    }

    @Override
    public Post findById(int id) {
        return postRep.findById(id).orElse(null);
    }

    @Override
    public Post save(Post post) {
        return postRep.save(post);
    }

    @Override
    public void deleteById(int id) {
         postRep.deleteById(id);
    }

   @Override
   public List<Post> findByTitle(String title){
        return postRep.findByTitleContainingIgnoreCase(title);
   }

    @Override
    public List<Post> findByAddress(String address) {
        return postRep.findByAddressContainingIgnoreCase(address);
    }
    public List<Post> allPostByListId (List<Integer> ids){
        return postRep.findAllById(ids);
    }

    @Override
    public List<Post> topPostApplied() {
     List<Integer> listId = postRep.topAppliedPost();
     return allPostByListId(listId);
    }


@Override
public Page<Post> fetchPost(Pageable pageable){
        return postRep.findAll(pageable);
}

}
