export class ContextStore {
    context = {};
    setKey(key, value) {
        context[key] = value;
    }
    getKey(key) {
        return context[key];
    }
}
export const context = new ContextStore();