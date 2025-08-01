export interface Post {
    id:number;
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

export interface CreatePostRequest{
    content:string;
}
