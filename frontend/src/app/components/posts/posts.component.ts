import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";

@Component({
  selector: 'app-posts',
  imports: [FormsModule, SidebarComponent],
  templateUrl: './posts.component.html',
  styleUrl: './posts.component.css'
})
export class PostsComponent implements OnInit {


  @ViewChild('newpost')  newpost : ElementRef | undefined;


items=[1,2,3,4,5];

  ngOnInit(): void {
    
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