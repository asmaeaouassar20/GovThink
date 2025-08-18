import { Injectable, inject } from '@angular/core';
import { CreateCommentRequest, CreatePostRequest, Post, MyComment, PostResponse } from '../interfaces/posts';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { UserDTO } from '../interfaces/users';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})

export class PostService {
  private apiUrl = 'http://localhost:8080/api/posts';


  http = inject(HttpClient)
  authService = inject(AuthService);

  getAllPosts() : Observable<Post[]>{
    return this.http.get<Post[]>(`${this.apiUrl}`);
  } 


  getPost(id : number) : Observable<PostResponse>{
    return this.http.get<PostResponse>(`${this.apiUrl}/${id}`);
  }

  createPost(post : CreatePostRequest) : Observable<Post>{
    return this.http.post<Post>(`${this.apiUrl}`,post,{
      headers: this.authService.getHeaders()
    });
  }


  addLike(postId : number) : Observable<Post> {
    return this.http.post<Post>(`${this.apiUrl}/${postId}/likes`, null);
  }
  


  addComment(id : number, comment : CreateCommentRequest) : Observable<MyComment>{
    return this.http.post<MyComment>(`${this.apiUrl}/${id}/comments`,comment,{
      headers : this.authService.getHeaders()
    });
  }


  getSavedPosts():Observable<Post[]>{
    return this.http.get<Post[]>(`${this.apiUrl}/saved-posts`,{
      headers :this.authService.getHeaders()
    });
  }


  savePost(postId:number):Observable<UserDTO>{
    return this.http.post<UserDTO>(`${this.apiUrl}/${postId}/save`,{},{
      headers : {
        'Authorization' : `Bearer ${localStorage.getItem('token')}`
      }
    });
  }

  unsavePost(postId:number):Observable<UserDTO>{
    return this.http.delete<UserDTO>(`${this.apiUrl}/${postId}/unsave`,{
      headers : {
        'Authorization' : `Bearer ${localStorage.getItem('token')}`
      }
    });
  }

  getPublishedPosts() : Observable<Post[]>{
    return this.http.get<Post[]>(`${this.apiUrl}/my-posts`,{
      headers : {
        'Authorization' : `Bearer ${localStorage.getItem('token')}`
      }
    })
  }

}
