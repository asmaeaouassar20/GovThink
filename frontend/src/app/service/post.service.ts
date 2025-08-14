import { Injectable, inject } from '@angular/core';
import { CreateCommentRequest, CreatePostRequest, Post, MyComment, PostResponse } from '../interfaces/posts';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = 'http://localhost:8080/api/posts';

  http = inject(HttpClient)

  getAllPosts() : Observable<Post[]>{
    return this.http.get<Post[]>(`${this.apiUrl}`);
  } 


  getPost(id : number) : Observable<PostResponse>{
    return this.http.get<PostResponse>(`${this.apiUrl}/${id}`);
  }

  createPost(post : CreatePostRequest) : Observable<Post>{
    return this.http.post<Post>(`${this.apiUrl}`,post, {
      headers : {
        'Authorization' : `Bearer ${localStorage.getItem('token')}`
      }
    });
  }


  addLike(postId : number) : Observable<Post> {
    return this.http.post<Post>(`${this.apiUrl}/${postId}/likes`, null);
  }
  


  addComment(id : number, comment : CreateCommentRequest) : Observable<Comment>{
    return this.http.post<Comment>(`${this.apiUrl}/${id}/comments`,comment);
  }

}
