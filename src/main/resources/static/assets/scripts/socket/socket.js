// Create WebSocket connection.
export const socket = new WebSocket("ws://localhost:8080/state");
let context = {};
// Connection opened
socket.addEventListener("open", (event) => {
    console.log('Connected to server!');
    socket.send(JSON.stringify({
        "type": "STATE_REQUEST", "data": window.stateKeys
    }));
});

// Listen for messages
socket.addEventListener("message", (event) => {
    const data = JSON.parse(event.data);
    console.log("Message from server ", data);
    window.context.setKey(data.key, data.data);
    window.dispatchEvent(new CustomEvent(`${data.type}::${data.key}`, {bubbles: true, detail: data.data}));
});

socket.addEventListener('close', (event) => {
    console.log('WebSocket closed:', event);
    context = {
        ...context, ...event.data
    }
});

socket.addEventListener('error', (error) => {
    console.error('WebSocket error:', error);
    context = {
        ...context, ...error.data
    }
});