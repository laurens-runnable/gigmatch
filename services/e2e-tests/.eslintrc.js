module.exports = {
  env: {
    es2021: true,
  },
  extends: 'standard-with-typescript',
  overrides: [],
  parserOptions: {
    project: './tsconfig.json',
    ecmaVersion: 'latest',
    sourceType: 'module',
  },
  rules: {
    'comma-dangle': 0,
    '@typescript-eslint/comma-dangle': 0,
    '@typescript-eslint/space-before-function-paren': 0,
    '@typescript-eslint/indent': 0,
  },
}
