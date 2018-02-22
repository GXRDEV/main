module.exports = {
  root: true,
  parser: 'babel-eslint',
  env: {
    browser: true,
    node: true
  },
  extends: 'standard',
  // required to lint *.vue files
  plugins: [],
  // add your custom rules here
  rules: {
    "indent": ["error", 4]
  },
  globals: {}
}