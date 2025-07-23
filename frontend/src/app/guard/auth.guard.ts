import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { inject } from '@angular/core';

/**
 *  Un guard Angular ( plus précisément un CanActivateFn ), utilisé pour contrôler 
 * l'accès à des routes spécifiques dans une applicaion Angular
 */

export const authGuard: CanActivateFn = (route, state) => {
 

  const authService = inject(AuthService);
  const router = inject(Router);
console.log(" ggg "+authService.isLoggedIn())

  // Vérification  de l'état de connexion
  if(authService.isLoggedIn()){
    return true;
  }
  

  router.navigate(['/home']);
  return false; // Refuser l'accès à la route demandée


};
