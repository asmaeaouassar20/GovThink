import { Component, OnInit } from '@angular/core';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";
import { DatePipe, NgIf } from '@angular/common';
import { ProfileService } from '../../service/profile.service';
import { UpdateProfile, UserProfile } from '../../interfaces/profile';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { Post } from '../../interfaces/posts';
import { PostService } from '../../service/post.service';

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
  previewPhotoUrl : string | null = null; //Nouvelle propriété pour prévisualier la photo


  // Concernant le changement de l'email : si l'utilisateur a changé son eamil on oblige la reconnexion
  emailBefore='';

  // La photo sélectionnée
  selectedFile : File | null = null;

  // Les posts sauvegardés par l'utilisateur
  savedPosts : Post[] = [];
  publishedPosts : Post[] = [];

  savedPostsView=false;
  publishedPostView=false;



  constructor(private profileService : ProfileService, 
            private formBuilder : FormBuilder,
            private router : Router,
            private postService : PostService
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
          this.previewPhotoUrl=null;
          this.selectedFile=null;
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


  
  // Méthode corrigée pour uploader la photo de profil
  uploadProfilePicture(): Observable<any> {
    if (!this.selectedFile) {
      return of(null);
    }

    // Créer le FormData correctement
    const formData = new FormData();
    formData.append('file', this.selectedFile, this.selectedFile.name);

    return this.profileService.uploadProfilePicture(formData);
  }

  // Méthode pour la sélection de fichier
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      
      
      // Vérifier le type de fichier
      if (file.type.startsWith('image/')) {
        // Vérifier la taille du fichier (par exemple, max 5MB)
        if (file.size > 5 * 1024 * 1024) {
          this.errorMessage = 'Le fichier est trop volumineux (max 5MB)';
          this.selectedFile = null;
          return;
        }

        this.selectedFile = file;
        
        // Créer une URL de prévisualisation
        const reader = new FileReader();
        reader.onload = (e) => {
          this.previewPhotoUrl = e.target?.result as string;
          this.photoProfilUrl = this.previewPhotoUrl;
        };
        reader.readAsDataURL(file);
        
        // Effacer les messages d'erreur
        this.errorMessage = '';
      } else {
        this.errorMessage = 'Veuillez sélectionner un fichier image valide (JPG, PNG, GIF, etc.)';
        this.selectedFile = null;
      }
    }
  }



  saveChanges(): void {    
    // Reset des messages
    this.errorMessage = '';
    this.successMessage = '';
    
    // Si une nouvelle photo a été sélectionnée, l'uploader d'abord
    if (this.selectedFile) {
      this.uploadProfilePicture().subscribe({
        next: (response) => {
          if (response && response.success) {
            this.successMessage = 'Photo mise à jour avec succès';
            this.selectedFile = null;
            this.previewPhotoUrl = null;
            // Après l'upload de la photo, mettre à jour les autres infos
            this.changeProfileinfos();
          } else {
            this.errorMessage = response?.message || 'Erreur lors de l\'upload de la photo';
          }
        },
        error: (error) => {
        }
      });
    } else {
      // Si pas de nouvelle photo, vérifier si on doit supprimer la photo existante
      if (this.photoProfilUrl === '') {
        this.deleteProfilePicture();
      }
      // Mettre à jour les autres informations
      this.changeProfileinfos();
    }

    this.isEditingProfile = false;
    // Recharger le profil à la fin
    setTimeout(() => this.loadUserProfile(), 1000);
  }




  

  // Récupération des postes sauvegradés
  getSavedPosts(){
    this.savedPostsView=!this.savedPostsView;
    this.publishedPostView=false;
    this.postService.getSavedPosts().subscribe({
      next : (savedPosts) => {
        this.savedPosts=savedPosts;
      },
      error : (erreur) => {
        console.log("Erreur lors de la récupérations des posts sauvegardé. Erreur : ");
        console.log(erreur)
      }
    })
  }

  // Annuler la sauvegrade d'un post
  unsavePost(postId:number){
    this.postService.unsavePost(postId).subscribe({
      next : (userDto) => {
        console.log(userDto);
        this.getSavedPosts();
        this.savedPostsView=true;
      },
      error : (erreur) => {
        console.log("Erreur lors de unsave post. Erreur : ");
        console.log(erreur);
      }
    })
  }

  // Récupérer les postes publiés
  getPublishedPosts(){
    this.publishedPostView=!this.publishedPostView;
    this.savedPostsView=false;
    this.postService.getPublishedPosts().subscribe({
      next : (publishedPosts) => this.publishedPosts=publishedPosts,
      error : (erreur) => {
        console.log("Erreur lors de la récupération des postes publiés. Erreur : ");
        console.log(erreur);
      }
    })
  }

  

}
