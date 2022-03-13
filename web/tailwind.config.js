module.exports = {
  content: ["./src/**/*.cljs"],
  darkMode: false, // or 'media' or 'class'
  theme: {
      container: {
          center: true,
      },
      extend: {
          colors: {
              primary: {
                  light: '#A5B4FC',
                  DEFAULT: '#6200EE',
                  dark: '#3700B3',
              },
              secondary: {
                  light: '#FDBA74',
                  DEFAULT: '#F97316',
                  dark: '#C2410C',
              },
              on: {
                  primary: '#FFFFFF',
                  secondary: '#000000',
              },
              neutral: {
                  light: '#E5E7EB',
                  DEFAULT: '#9CA3AF',
                  dark: '#6B7280',
              }
          },
      },
  },
  variants: {
      extend: {
          backgroundColor: ['active'],
      },
  },
  plugins: [
      require('@tailwindcss/forms'),
      require('@tailwindcss/typography'),
  ],
}
