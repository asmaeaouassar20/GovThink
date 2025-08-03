export interface UserProfile {
    id : number;
    nom : string;
    prenom : string;
    email : string;
    bio : string;
}

export interface ApiResponse<T>{
    success : boolean;
    message : string;
    data : T;
}

export interface ChangePassword{
    currentpassword : string;
    newPassword : string;
    confirmPassword : string;
}