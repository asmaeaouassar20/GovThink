export interface Post {
    id:number;
    title : string;
    content:string;
    authorName:string;
    createdAt:string;
    updatedAt:string;
    commentCount:number; 
    likesCount:number;
}
export interface PostResponse {
    id:number;
    title : string;
    content:string;
    authorName:string;
    createdAt:string;
    updatedAt:string;
    commentCount:number; 
    likesCount:number;
    comments : MyComment[]
}

export interface MyComment {
    id:number;
    content:string;
    authorName:string;
    createdAt:string;
    updatedAt:string; 
}

export interface CreateCommentRequest{
    content:string;
}


// Le payload pour créer un post 
// Payload : les données qu'on envoie au backend dans la requête
export interface CreatePostRequest{
    title?:string;
    content:string;
}
