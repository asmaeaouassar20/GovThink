import { Component } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-register',
  imports: [FormsModule,NgIf,RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  userData = { nom:'', prenom:'', email:'', password:'',passwordconfirm:'' , bio:''};
  message = '';
  error = '';

  mdpVisible : boolean = false;
  confirmMdpVisible : boolean = false;

  // Pour l'ajout de la photo de profil et de la biographie
  isContentVisible = true;
  showPopupPhotoProfil = false;
  showPopupBiographie = false;

  selectedPhoto : File | null = null; // la photo de profil séléctionné


 
  constructor(private authService:AuthService,
            private router : Router
  ){}

  onSignup() : void {
    if(!this.isFormValid()){
      return;
    }
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

  isFormValid() : boolean{
    if(this.userData.nom.length<=1){
      alert('Le nom doit contenir au moins 2 caractères');
      return false;
    }

    if(this.userData.prenom.length<=1){
      alert('Le prénom doit contenir au moins 2 caractères');
      return false;
    }
    if(!this.userData.email || !this.userData.email.endsWith("@gmail.com")){
      alert('Email invalid');
      return false;
    }
    if(this.userData.password!==this.userData.passwordconfirm){
      alert('Probleme dans la confirmation du password');
      return false;
    }
    return true;
  }

} 
