import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";
import { Post, PostResponse } from '../../interfaces/posts';
import { PostService } from '../../service/post.service';

@Component({
  selector: 'app-my-posts',
  imports: [SidebarComponent,DatePipe],
  templateUrl: './my-posts.component.html',
  styleUrl: './my-posts.component.css'
})
export class MyPostsComponent implements OnInit {


  myPosts : Post[] = [];
  viewComments=false;
  selectedPost : PostResponse =undefined;

  constructor(private postService:PostService){}
  

  ngOnInit(): void {
    this.getMyPosts();
  }



  getMyPosts(){
      this.postService.getPublishedPosts().subscribe({
        next : (posts) => this.myPosts=posts,
        error : (erreur) => {
          console.log("Erreur lors de la récupération de mes postes. Erreur : ");
          console.log(erreur);
        }
      })
  }



  openViewCommentsWindow(postId:number){
    this.viewComments=true;
    this.postService.getPost(postId).subscribe({
      next : (post) => this.selectedPost=post,
      error : (erreur) => {
        console.log("erreur lors de la récupération du post avec id: "+postId+". ERREUR: ");
        console.log(erreur);
      }
    })
  }
  closeViewCommentsWindow(){
    this.viewComments=false;
    this.selectedPost=undefined;
  }

}
