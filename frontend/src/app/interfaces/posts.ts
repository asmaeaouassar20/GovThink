export interface Post {
    id:number;
    title : string;
    content:string;
    authorName:string;
    createdAt:string;
    updatedAt:string;
    commentCount:number; 
}

export interface Comment {
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
