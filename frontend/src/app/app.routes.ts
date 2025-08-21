import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { RegisterComponent } from './authentification/register/register.component';
import { LoginComponent } from './authentification/login/login.component';
import { AccueilComponent } from './components/accueil/accueil.component';
import { DashboardComponent } from './components/dashboard/dashboard/dashboard.component';
import { TablesComponent } from './components/dashboard/dashboard/tables/tables.component';
import { ChartsComponent } from './components/dashboard/dashboard/charts/charts.component';
import { ProfilesComponent } from './components/profiles/profiles.component';
import { UsersProjectsComponent } from './components/users-projects/users-projects.component';
import { PostsComponent } from './components/posts/posts.component';
import { AproposComponent } from './components/apropos/apropos.component';
import { MyProfileComponent } from './components/my-profile/my-profile.component';
import { MyPostsComponent } from './components/my-posts/my-posts.component';
import { MyProjectsComponent } from './components/my-projects/my-projects.component';
export const routes: Routes = [

    {path:'', redirectTo:'home', pathMatch:'full'},

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
    { path : 'users-projects', component:UsersProjectsComponent},
    { path : 'posts' , component:PostsComponent},
    { path : 'apropos', component:AproposComponent},
    { path : 'my-profile' , component:MyProfileComponent},
    { path : 'my-posts' , component:MyPostsComponent},    
    { path : 'my-projects', component:MyProjectsComponent},

    
    {path:'**', redirectTo:'/home'}


];
