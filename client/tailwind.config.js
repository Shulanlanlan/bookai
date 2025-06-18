/** @type {import('tailwindcss').Config} */
export default {
    content: [
        "./index.html",
        "./src/**/*.{vue,js,ts,jsx,tsx}", // 重点是加上 vue 支持
    ],
    theme: {
        extend: {},
    },
    plugins: [],
}

