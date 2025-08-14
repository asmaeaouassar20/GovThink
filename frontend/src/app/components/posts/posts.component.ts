import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe, NgIf } from '@angular/common';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";
import { MyComment, CreatePostRequest, Post, PostResponse, CreateCommentRequest } from '../../interfaces/posts';
import { PostService } from '../../service/post.service';
import { error } from 'console';

@Component({
  selector: 'app-posts',
  imports: [FormsModule, SidebarComponent, DatePipe, NgIf],
  templateUrl: './posts.component.html',
  styleUrl: './posts.component.css'
})
export class PostsComponent implements OnInit {


  @ViewChild('newpost')  newpost : ElementRef | undefined;
  posts : Post[] = [];
  newPost : CreatePostRequest = { title : '', content : '' } ;
  selectedPost : PostResponse | undefined;
  addingComment=false;
  viewComments=false;

  newComment : CreateCommentRequest = {content:''};
 


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









  addLike(postId:number){
    this.postService.addLike(postId).subscribe({
      next : (post) => this.loadPosts(),
      error : (erreur) => console.log("erreur lors de l'ajout d'un like. Erreur: "+erreur)
    })
  }






  openViewCommentsWindow(postId:number){
    this.viewComments=true;
    this.postService.getPost(postId).subscribe({
      next : (post) => this.selectedPost=post,
      error : (erreur) => console.log("erreur lors de la récupération du post avec id "+postId+". Erreur : "+erreur)
    })
  }
  closeViewCommentsWindow(){
    this.viewComments=false;
    this.selectedPost=undefined;
    this.loadPosts();
  }





  openAddCommentWindow(postId:number){
    this.addingComment=true;
    this.postService.getPost(postId).subscribe({
      next : (post) => this.selectedPost=post ,
      error : (erreur) => console.error("erreur lors de la récupération du post avec id "+postId+". Erreur : "+erreur)
    })
  }
  closeAddCommentWindow(){
    this.addingComment=false;
    this.selectedPost=undefined;
    this.loadPosts();
  }

  




  // pour créer un nouveau post
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




  // Pour publier un commentaire selon l'id du poste sélectionné 
  publishComment(postId:number){
    if(this.newComment.content.length>500){
      alert("le commentaire ne doit passer 500 caractères !!");
      return;
    }
    this.postService.addComment(postId,this.newComment).subscribe({
      next : (comment) => {
        this.selectedPost.comments.push(comment);
        this.addingComment=false;
        this.openViewCommentsWindow(postId);
      },
      error : (erreur) => {
        console.error("erreur lors de l'ajout du commentaire au post d'id "+postId+". Erreur: ");
        console.log(erreur)
      }
    })

  }


  
  
}