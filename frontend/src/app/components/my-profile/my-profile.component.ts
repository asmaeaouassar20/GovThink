import { Component, OnInit } from '@angular/core';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";
import { NgIf } from '@angular/common';
import { ProfileService } from '../../service/profile.service';
import { UserProfile } from '../../interfaces/profile';

@Component({
  selector: 'app-my-profile',
  imports: [SidebarComponent, NgIf],
  templateUrl: './my-profile.component.html',
  styleUrl: './my-profile.component.css'
})
export class MyProfileComponent implements OnInit{
  isEditiong = true;

  userProfile : UserProfile | null = null;

  constructor(private profileService : ProfileService){}

  ngOnInit(): void {
      
  }



  
  // Charger le profil de l'utilisateur
  loadUserProfile() : void{
    this.profileService.getCurrentProfile().subscribe({
      next : (response) =>{
        if(response.success && response.data){
          this.userProfile=response.data;
        }
      }
    })
  }





  // Remplir le formulaire avec les données du profil
  private populateProfieForm(): void{
    
  }


}
