import {InvalidTokenError, jwtDecode} from 'jwt-decode';

export function getDecodedJwt(token) {
    if (token == null) {
        throw new InvalidTokenError("Error decoding jwt: No token")
    }
    const decodedToken = jwtDecode(token)
    return decodedToken
}



export function checkJwtExpired() {
    const token = localStorage.getItem("token")
    let decodedToken = getDecodedJwt(token)

    let currentDate = new Date()
    if (decodedToken.exp * 1000 < currentDate.getTime()) {
        console.log("jwt token expired")
        return true
    }
    else {
        return false
    }
}


export function getJwt() {
    return localStorage.getItem("token")
}