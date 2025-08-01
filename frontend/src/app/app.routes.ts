import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { RegisterComponent } from './authentification/register/register.component';
import { LoginComponent } from './authentification/login/login.component';
import { AccueilComponent } from './components/accueil/accueil.component';
import { DashboardComponent } from './components/dashboard/dashboard/dashboard.component';
import { TablesComponent } from './components/dashboard/dashboard/tables/tables.component';
import { ChartsComponent } from './components/dashboard/dashboard/charts/charts.component';
import { ProfilesComponent } from './components/profiles/profiles.component';
import { PostsComponent } from './components/posts/posts.component';
export const routes: Routes = [

    {path:'', redirectTo:'/home', pathMatch:'full'},

    { path:'home' , component : HomeComponent}, //page d'acceuil
    { path:'register' , component : RegisterComponent},
    { path:'login', component:LoginComponent},




    // Composants
    { path : 'accueil', component:AccueilComponent},
    {
        path: 'dashboard',
        component : DashboardComponent,  // Contient les boutons et le <router-outlet>
        children : [
            { path: 'tables', component: TablesComponent},
            { path: 'charts', component: ChartsComponent},
            { path:'', redirectTo:'tables',pathMatch:'full'}
        ]
    },
    { path : 'profiles' , component:ProfilesComponent},
    { path:'posts' , component:PostsComponent},


    
    {path:'**', redirectTo:'/home'}


];
