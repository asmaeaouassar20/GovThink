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

  userData = { nom:'', prenom:'', email:'', password:'' };
  message = '';
  error = '';

 
  constructor(private authService:AuthService,
            private router : Router
  ){}

  onSignup() : void {
    this.authService.signup(this.userData).subscribe({
      next : (response) =>{
        this.message = 'Inscription réussie';
        setTimeout( ()=> this.router.navigate(['/login']), 1500);
      },
      error :(error) =>{
        this.error = 'Erreur lors de l\'inscription';
      }
    })
  }

} 
