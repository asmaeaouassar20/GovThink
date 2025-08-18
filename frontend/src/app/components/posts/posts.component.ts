import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe, NgIf,NgClass } from '@angular/common';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";
import { CreatePostRequest, Post, PostResponse, CreateCommentRequest } from '../../interfaces/posts';
import { PostService } from '../../service/post.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'app-posts',
  imports: [FormsModule, SidebarComponent, DatePipe, NgIf, NgClass],
  templateUrl: './posts.component.html',
  styleUrl: './posts.component.css'
})
export class PostsComponent implements OnInit {


  @ViewChild('newpost')  newpost : ElementRef | undefined;
  posts : Post[] = [];
  filteredPosts : Post[] = [];
  newPost : CreatePostRequest = { title : '', content : '' } ;
  selectedPost : PostResponse | undefined;
  addingComment=false;
  viewComments=false;

  newComment : CreateCommentRequest = {content:''};

  savedPosts : Post[] = [] ;  //Récupérer les posts sauvegardés par l'utilisateur connecté
 

  // Pour la recherche
  searchTerm : string ='';  // Ce que l'utilisateur tape dans la barre de recherche
  searchResultsCount : number = 0;  // Nombre de résultats trouvés


  constructor(private postService:PostService,
              private sanitizer : DomSanitizer  // Angular bloque du HTML "dangereux" (pour éviter les failles XSS). DomSanitizer permet de dire à Angular "Ce HTML est sûr, affiche-le"
  ){}


  ngOnInit(): void {
    this.loadPosts();
    this.getSavedPosts();
  }

  loadPosts(){
    this.postService.getAllPosts().subscribe({
      next : (data) => {
        this.posts=data;
        this.filteredPosts=[...this.posts];
      },
      error : (erreur) => console.error('Erreur lors de la récupération de tous les posts : '+erreur)
    })
  }

  publishNewPost(){
   this.postService.createPost(this.newPost).subscribe({
    next : (postDtoCreated) => {
      this.posts.push(postDtoCreated);
      this.resetFormNewPost();
      this.closeModalToCreateNewPost();
      this.loadPosts();
    },
    error : (erreur) => console.error("erreur lors de la creation d'un nouveau post", erreur)
   })
  }


  resetFormNewPost(){
    this.newPost.title='';
    this.newPost.content='';
  }









  addLike(postId:number){
    this.postService.addLike(postId).subscribe({
      next : (post) => this.loadPosts(),
      error : (erreur) => console.log("erreur lors de l'ajout d'un like. Erreur: "+erreur)
    })
  }






  openViewCommentsWindow(postId:number){
    this.viewComments=true;
    this.postService.getPost(postId).subscribe({
      next : (post) => {
        this.selectedPost=post;
        console.log(this.selectedPost);
      },
      error : (erreur) => console.log("erreur lors de la récupération du post avec id "+postId+". Erreur : "+erreur)
    })
  }
  closeViewCommentsWindow(){
    this.viewComments=false;
    this.selectedPost=undefined;
    this.loadPosts();
  }





  openAddCommentWindow(postId:number){
    this.addingComment=true;
    this.postService.getPost(postId).subscribe({
      next : (post) => this.selectedPost=post ,
      error : (erreur) => console.error("erreur lors de la récupération du post avec id "+postId+". Erreur : "+erreur)
    })
  }
  closeAddCommentWindow(){
    this.addingComment=false;
    this.selectedPost=undefined;
    this.loadPosts();
  }

  




  // pour créer un nouveau post
  openModalToCreateNewPost(){
    if(this.newpost){
      this.newpost.nativeElement.style.display='block';
    }
  }
  closeModalToCreateNewPost(){
    if(this.newpost){
      this.newpost.nativeElement.style.display='none';
    }
  }




  // Pour publier un commentaire selon l'id du poste sélectionné 
  publishComment(postId:number){
    if(this.newComment.content.length>500 || this.newComment.content.length<8){
      alert("le commentaire ne doit passer 500 caractères !!");
      return;
    }
    this.postService.addComment(postId,this.newComment).subscribe({
      next : (comment) => {
        this.selectedPost.commentsDto.push(comment);
        this.addingComment=false;
        this.openViewCommentsWindow(postId);
      },
      error : (erreur) => {
        console.error("erreur lors de l'ajout du commentaire au post d'id "+postId+". Erreur: ");
        console.log(erreur)
      }
    })

  }




  getSavedPosts(){
    this.postService.getSavedPosts().subscribe({
      next : (savedPosts) =>  this.savedPosts=savedPosts,
      error : (erreur) => {
        console.error("erreur lors de la récupération des posts sauvegardés. Erreur : ");
        console.log(erreur);
      }
    })
  }
  containsPost(id:number){
    for(let post of this.savedPosts){
      if(post.id===id) return true;
    }
    return false;
  }



  savePost(postId:number){
    this.postService.savePost(postId).subscribe({
      next : (user) => {
          this.getSavedPosts();
      },
      error : (erreur) =>{
        console.log("erreur lors de la sauvegarde du post. Erreur : ");
        console.log(erreur);
      }
    })

  }

  unsavePost(postId:number){
    this.postService.unsavePost(postId).subscribe({
      next : (user) => {
        this.getSavedPosts();
      },
      error : (erreur) => {
        console.log("erreur lors de la sauvegarde du post. Erreur : ");
        console.log(erreur);
      }
    })
  }




  /**
   * Méthode appelée lors de la saisie dans la barre de recherche
   * @param event : l'événementde saisie
   */
  onSearchInput(event : any) : void{
    this.searchTerm = event.target.value; //récupérer le texte tapé
    this.updateSearchResultsCount();  // Mettre à jour le nombre de résultats
    this.filterPosts();
  }


  /**
   * Met à jour le nombre de résultats de recherche
   */
  private updateSearchResultsCount() : void{
    if(!this.searchTerm.trim()){
      this.searchResultsCount=0;
      return;
    }
    let count =0;
    const searchLower = this.searchTerm.toLowerCase();

    // Recherche dans les posts
    this.posts.forEach( post => {
      if(
        this.containsSearchTerm(post.title,searchLower) ||
        this.containsSearchTerm(post.content,searchLower) ||
        this.containsSearchTerm(post.authorName,searchLower) 
      ){
        count ++;
      }
  });
  this.searchResultsCount=count;
  }



  /**
   *  Vérifie si un texte le terme de ercherche
   * @param text - Le texte à vérifier
   * @param searchTerm  - Le terme de recherche en miniscule
   * @returns true si le texte contient le terme
   */
  private containsSearchTerm(text:string, searchTerm:string) : boolean {
    if (!text || !searchTerm) return false;
    return text.toLowerCase().includes(searchTerm);
  }





/**
 * Fonction qui surligne un terme recherché dans un texte
 * @param text 
 * @param searchTerm - Le mot à surligner
 * @returns safeHtml ( du HTML "sécurisé" que Angular acceptera d'afficher)
 */
  highlightText(text:string, searchTerm:string) : SafeHtml {

    // Si le texte est vide, on retourne une chaîne vide
    if (!text) return '';

    // Si aucun mot n'est recherché  ->  on retourne juste le texte original échappé
    // escapeHtml()  empêche qu'un texte comme "<b>gras</b>" soit interprété comme du HTML
    if(!searchTerm || !searchTerm.trim()){
      return this.sanitizer.bypassSecurityTrustHtml(this.escapeHtml(text));
    }

    // On échappe les caractères spéciaux HTML dans le texte
    // Ex : "<div>" devient "&lt;div&gt;"
    const escapedText = this.escapeHtml(text);


    // On échappe les caractères spéciaux du terme pour éviter de casser la regex
    // Ex: si le mot cherché est c++, on le transforme en "c\+\+"
    const escapedSearchTerm = this.escapeRegex(searchTerm.trim());


    // On construit une expression régulière qui cherche toutes les occurrences du mot
    // 'gi' signifie : 
    // g = global (chercher toutes les occurrence)
    // i = insensitive (ignorer majuscule/mnuscule)
    const regex = new RegExp(`(${escapedSearchTerm})`,'gi');

    // On remplace chaque occurence trouvée par le même text entouré de <mark>
    // Exemple : "Bonjour Asmae" avec searchTerm="asmae" devient "Bonjour <mark class='bg-danger'>Asmae</mark>"
    const highlightedText = escapedText.replace(regex,'<mark>$1</mark>');

    // On dit à Angular : "Ce HTML est sûr, affiche-le tel quel"
    return this.sanitizer.bypassSecurityTrustHtml(highlightedText);
  }



  /**
   * échappe les caractères HTML spécieux
   * @param text 
   * EXEMPLE1 : "<b>gras</b>" => "&lt;b&gt;gras&lt;/b&gt;"
   * EXEMPLE1 : escapeHtml("Bonjour <b>Ali</b>") donne  Bonjour &lt;b&gt;Ali&lt;/b&gt;
   */
  private escapeHtml(text:string) : string {
      const div = document.createElement('div'); // Créer une balise <div>
      div.textContent=text; // Mettre le texte à l'intérieur
      return div.innerHTML;
  }




  /**
   * échappe les caractères spéciaux des regex
   * @param string 
   * EXEMPLE : "c++" -> "c\+\+"
   */
  private escapeRegex(string:string) : string{
      return string.replace(/[.*+?^${}()|[\]\\]/g,'\\$g');  // remplacer tous les symboles spéciaux de regx par une version <<protégée>> (précédée d'un \)
  }




  // filtrer les posts selon le mot-clé de recherche
  filterPosts(){
    if(!this.searchTerm || this.searchTerm.trim().length==0){
      this.filteredPosts=[...this.posts]; // si pas de mot clé, afficher tous les posts
      return;
    }

    const keyword = this.searchTerm.toLowerCase().trim();

    this.filteredPosts=this.posts.filter( post => {

      // recherche dans le titre
      const titleMatch = post.title && post.title.toLowerCase().includes(keyword);


      // recherche dans le contenu 
      const contentMatch = post.content && post.content.toLowerCase().includes(keyword);

      // recherche dans le nom de l'auteur
      const authorNameMatch = post.authorName && post.authorName.toLowerCase().includes(keyword);

      return titleMatch || contentMatch || authorNameMatch;

    });

  }


  
}