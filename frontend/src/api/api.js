import { getJwt } from "../util.js/jwtUtils"

const apiUrl = import.meta.env.VITE_API_URL

async function makeApiRequest({ 
    endpoint, 
    method = "GET", 
    body = null, 
    requiresAuth = false, 
    customHeaders = {},
}) {
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
            body,
        })
    
        const data = await response.json().catch(() => null)
        console.log("data", data)
        if (!response.ok) {
            const message = data?.error || "An unexpected error occurred."
            console.log(data)
            throw new Error(message)
        }
    
        return data
    } catch (error) {
        if (error instanceof TypeError) {
            throw new Error("An unexpected Error occurred.")
        }
        throw error
    }
    
} 


export {makeApiRequest}
    


