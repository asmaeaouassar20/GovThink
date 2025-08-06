import { Component, OnInit } from '@angular/core';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";
import { DatePipe, NgIf } from '@angular/common';
import { ProfileService } from '../../service/profile.service';
import { UpdateProfile, UserProfile } from '../../interfaces/profile';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-my-profile',
  imports: [SidebarComponent, NgIf, ReactiveFormsModule, DatePipe],
  templateUrl: './my-profile.component.html',
  styleUrl: './my-profile.component.css'
})
export class MyProfileComponent implements OnInit{
  isEditingProfile = false;

  // Données du profile
  userProfile : UserProfile | null = null;

  // Formulaire
  profileForm : FormGroup;


  // Message
  errorMessage = '';
  successMessage = '';


  // Pour l'affichage de la photo de profil
  photoProfilUrl='';


  // Concernant le changement de l'email : si l'utilisateur a changé son eamil on oblige la reconnexion
  emailBefore='';



  constructor(private profileService : ProfileService, 
            private formBuilder : FormBuilder,
            private router : Router
          ){
    this.profileForm=this.createProfileForm();
  }

  ngOnInit(): void {
      this.loadUserProfile();
  }



  
  // Charger le profil de l'utilisateur
  loadUserProfile() : void{
    this.profileService.getCurrentProfile().subscribe({
      next : (response) =>{
        if(response.success && response.data){
          this.userProfile=response.data;
          this.photoProfilUrl=`http://localhost:8080${this.userProfile.profilePictureUrl}`;
          this.populateProfieForm();
          this.emailBefore=response.data.email;
        }else{
          this.errorMessage = response.message || 'Erreur lors du chargement du profil';
        }
      },
      error : (error) =>{
        this.errorMessage = 'Erreur lors du chargement du profil';
        console.error('erreur : '+error);
      }
    });
  }


  // Remplir le formulaire avec les données du profil
  private populateProfieForm(): void{
    if(this.userProfile){
      this.profileForm.patchValue({
        email : this.userProfile.email,
        nom : this.userProfile.nom || '',
        prenom : this.userProfile.prenom  || '',
        bio : this.userProfile.bio  || ''
      });
    }
  }



  // Activer le mode édition du profile
  startEditingProfile() : void {
    this.loadUserProfile();
    this.isEditingProfile=true;
  }


  // Annuler l'édition du profil
  cancelEditingProfile() : void{
    this.isEditingProfile=false;
    this.loadUserProfile();
  }



  private createProfileForm() :FormGroup{
    return this.formBuilder.group({
      email : ['', [Validators.required, Validators.email]],
      nom : ['', [Validators.maxLength(100)]],
      prenom : ['', [Validators.maxLength(100)]],
      bio : ''
    });
  }




  // Sauvegarder le profil
  changeProfileinfos() : void {
    const updatedata : UpdateProfile = this.profileForm.value;
    
    this.profileService.updateProfile(updatedata).subscribe({
      next : (response)=>{
        if(response.success && response.data){
          this.userProfile=response.data;
          this.successMessage = 'Profil mis à jour avec succès';
           if(this.userProfile.email!=this.emailBefore){
            alert('Reconnexion nécessaire ...')
            this.router.navigate(['/login']);
            }
        }else{
          this.errorMessage = response.message || 'Erreur lors de la mise à jour du profil';
        }
      },

      error : (error)=>{
        this.errorMessage = 'Erreur lors de la mise à jour du profil';
      }
    })
  }


  deleteProfilePicture(){
    this.profileService.deleteProfilPicture().subscribe({
      next: (res)=>{
        this.successMessage='Photo de profil mis à jour avec succès';
        this.loadUserProfile();
      },

      error: (erreur) =>{
        this.errorMessage = 'Erreur lors de la suppression de la photo de profil';
      }
    })
  }

  deleteProfilePictureLocaly(){
    this.userProfile.profilePictureUrl='';
    this.photoProfilUrl='';
    console.log("delete profil picture")
  }




  saveChanges(){
    if(this.photoProfilUrl==''){
      this.deleteProfilePicture();
    }
   this.changeProfileinfos();
   this.loadUserProfile();
   this.isEditingProfile=false;
  }

}
