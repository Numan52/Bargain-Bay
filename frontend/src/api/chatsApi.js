import { makeApiRequest } from "./api";


function getAllContacts() {
    return makeApiRequest({
        endpoint: `/chats/contacts`,
        method: "GET",
        requiresAuth: true
    })
}


function getChatMessages(chatId) {
    return makeApiRequest({
        endpoint: `/chats/${chatId}/messages`,
        method: "GET",
        requiresAuth: true
    })
}


function markChatAsSeen(chatId) {
    return makeApiRequest({
        endpoint: `/chats/${chatId}/seen`,
        method: "POST",
        requiresAuth: true
    })
}


export {getAllContacts, getChatMessages, markChatAsSeen}
