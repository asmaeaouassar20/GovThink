import { Component } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-register',
  imports: [FormsModule,NgIf],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  userData = { nom:'', prenom:'', email:'', password:'' , bio:''};
  message = '';
  error = '';

  // Pour l'ajout de la photo de profil et de la biographie
  isContentVisible = true;
  showPopupPhotoProfil = false;
  showPopupBiographie = false;

  selectedPhoto : File | null = null; // la photo de profil séléctionné


 
  constructor(private authService:AuthService,
            private router : Router
  ){}

  onSignup() : void {
    const formData = new FormData();

    // Ajouter les données utilisateur sous forme de chaîne JSON
    formData.append('userData',JSON.stringify(this.userData));

    if(this.selectedPhoto){
      formData.append('profilePicture', this.selectedPhoto);
    }

    this.authService.signup(formData).subscribe({
      next : (response) =>{
        this.message = 'Inscription réussie';
        setTimeout( ()=> this.router.navigate(['/login']), 1500);
      },
      error :(error) =>{
        this.error = 'Erreur lors de l\'inscription';
      }
    })
  }



  onFileSelected(event : any){
    this.selectedPhoto=event.target.files[0];
  }
  validateImage(){
    this.showPopupPhotoProfil=false;
  }
  validateBio(){
    this.showPopupBiographie=false;
  }

} 
