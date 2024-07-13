/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
          primary: '#242424',
          secondary: '#A3A3A3',
          tertiary: '#FAF9F6'
        },
    },
  },
  plugins: [],
}
