export const stateKeys = [];
function getStateKeyFromCustomTags() {
    console.log('getStateKeyFromCustomTags');
    const stateTags = document.getElementsByTagName('state');
    const keys = [];
    for (const tag of stateTags) {
        const key = tag.getAttribute('key');
        if (key) {
            keys.push(key);
        }
    }
    return keys;
}

window.addEventListener('DOMContentLoaded', () => {
    stateKeys.push(...getStateKeyFromCustomTags());
    console.log('Keys from custom state tags:', stateKeys);
});