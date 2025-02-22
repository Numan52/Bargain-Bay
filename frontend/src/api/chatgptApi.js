import { makeApiRequest } from "./api";

function askChatGpt({message = "", adId = null}) {
    return makeApiRequest({
        endpoint: `/ai/ask`,
        method: "POST",
        body: JSON .stringify({message, adId})
    })
}


export default askChatGpt
