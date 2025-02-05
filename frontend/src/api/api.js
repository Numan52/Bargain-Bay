import { getJwt } from "../jwtUtils"

const apiUrl = import.meta.env.VITE_API_URL

async function apiRequest(
    endpoint, 
    method = "GET", 
    body = null, 
    requiresAuth = false, 
    customHeaders = {}) 
{
    if (requiresAuth) {
        const token = getJwt()
        customHeaders["Authorization"] = `Bearer ${token}`
    }

    if (body && !(body instanceof FormData) && !customHeaders["Content-Type"]) {
        customHeaders["Content-Type"] = "application/json"
    }

    try {
        const response = await fetch(`${apiUrl}${endpoint}`, {
            method,
            headers: customHeaders,
            body
        })
    
        const data = await response.json().catch(() => null)
    
        if (!response.ok) {
            const message = data?.error || "An unexpected error occurred."
            console.log(data)
            throw new Error(message)
        }
    
        return data
    } catch (error) {
        throw new Error("An unexpected Error occurred.")
    }
    
} 


function loginUser(username, password) {
    return apiRequest("/login", "POST", JSON.stringify({username, password}))
}


function registerUser(firstName, lastName, username, password, email) {
    return apiRequest("/register", "POST", JSON.stringify({firstName, lastName, email, username, password}))
}


function postAd(form) {
    return apiRequest("/ads", "POST", form, true)
}


export {apiRequest, loginUser, registerUser, postAd}
    


