import { Component, OnInit } from '@angular/core';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";
import { DatePipe, NgIf } from '@angular/common';
import { ProfileService } from '../../service/profile.service';
import { UpdateProfile, UserProfile } from '../../interfaces/profile';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

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




  constructor(private profileService : ProfileService, private formBuilder : FormBuilder){
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
    this.isEditingProfile=true;
  }


  // Annuler l'édition du profil
  cancelEditingProfile() : void{
    this.isEditingProfile=false;
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
  saveProfile() : void {
    const updatedata : UpdateProfile = this.profileForm.value;
    
    this.profileService.updateProfile(updatedata).subscribe({
      next : (response)=>{
        if(response.success && response.data){
          this.userProfile=response.data;
          this.isEditingProfile=false;
          this.successMessage = 'Profil mis à jour avec succès';
        }else{
          this.errorMessage = response.message || 'Erreur lors de la mise à jour du profil';
        }
      },

      error : (error)=>{
        this.errorMessage = 'Erreur lors de la mise à jour du profil';
      }
    })
  }

  onFileSelected(event: Event){
    console.log(event)
  }

}
