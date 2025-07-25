import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { RegisterComponent } from './authentification/register/register.component';
import { LoginComponent } from './authentification/login/login.component';
import { AccueilComponent } from './components/accueil/accueil.component';
import { SidebarComponent } from './sidebars/sidebar/sidebar.component';
import { authGuard } from './guard/auth.guard';
import { DashboardComponent } from './components/dashboard/dashboard.component';

export const routes: Routes = [

    {path:'', redirectTo:'/home', pathMatch:'full'},

    { path:'home' , component : HomeComponent}, //page d'acceuil
    { path:'register' , component : RegisterComponent},
    { path:'login', component:LoginComponent},



    // Composants
    { path : 'accueil', component:AccueilComponent},
    { path : 'dashboard', component:DashboardComponent},

    
    {path:'**', redirectTo:'/home'}


];
