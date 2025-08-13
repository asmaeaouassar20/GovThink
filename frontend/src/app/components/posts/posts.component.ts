import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";
import { CreatePostRequest, Post } from '../../interfaces/posts';
import { PostService } from '../../service/post.service';

@Component({
  selector: 'app-posts',
  imports: [FormsModule, SidebarComponent, DatePipe],
  templateUrl: './posts.component.html',
  styleUrl: './posts.component.css'
})
export class PostsComponent implements OnInit {


  @ViewChild('newpost')  newpost : ElementRef | undefined;
  posts : Post[] = [];
  newPost : CreatePostRequest = { title : '', content : '' } ;


  constructor(private postService:PostService){}


  ngOnInit(): void {
    this.loadPosts();
  }

  loadPosts(){
    this.postService.getAllPosts().subscribe({
      next : (data) => this.posts=data,
      error : (erreur) => console.error('Erreur lors de la récupération de tous les posts : '+erreur)
    })
  }

  publishNewPost(){
   this.postService.createPost(this.newPost).subscribe({
    next : (postDtoCreated) => {
      this.posts.push(postDtoCreated);
      this.resetFormNewPost();
      this.closeModalToCreateNewPost();
    },
    error : (erreur) => console.error("erreur lors de la creation d'un nouveau post", erreur)
   })
  }


  resetFormNewPost(){
    this.newPost.title='';
    this.newPost.content='';
  }






  openModalToCreateNewPost(){
    if(this.newpost){
      this.newpost.nativeElement.style.display='block';
    }
  }
  closeModalToCreateNewPost(){
    if(this.newpost){
      this.newpost.nativeElement.style.display='none';
    }
  }
  
}