import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { RegisterComponent } from './authentification/register/register.component';
import { LoginComponent } from './authentification/login/login.component';
import { AccueilComponent } from './components/accueil/accueil.component';
import { SidebarComponent } from './sidebars/sidebar/sidebar.component';
import { authGuard } from './guard/auth.guard';

export const routes: Routes = [

    {path:'', redirectTo:'/home', pathMatch:'full'},

    { path:'home' , component : HomeComponent}, //page d'acceuil
    { path:'register' , component : RegisterComponent},
    { path:'login', component:LoginComponent},



    // Composants
    { path:'accueil', component:AccueilComponent,canActivate:[authGuard]},

    
    {path:'**', redirectTo:'/home'}


];
