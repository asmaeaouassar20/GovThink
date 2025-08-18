export interface Post {
    id:number;
    title : string;
    content:string;
    authorName:string;
    createdAt:string;
    commentCount:number; 
    likesCount:number;
    authorProfilePictureUrl:string;
}
export interface PostResponse {
    id:number;
    title : string;
    content:string;
    authorName:string;
    createdAt:string;
    commentCount:number; 
    likesCount:number;
    commentsDto : MyComment[]
}

export interface MyComment {
    id:number;
    content:string;
    authorName:string;
    createdAt:string;
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
