export interface Post {
    id:number;
    content:string;
    createdAt:Date;
    updatedAt:Date;
    commentCount:number;
}

export interface Comment {
    id:number;
    content:string;
    authorName:string;
    createdAt:Date;
    updatedAt:Date;
}

export interface CreateCommentRequest{
    content:string;
}

export interface CreatePostRequest{
    content:string;
}
