import { QuestionPair, WordPair } from './types';

export const COLORS = {
  background: '#0F0F14',
  card: '#1A1A22',
  primary: '#7C5CFF',
  secondary: '#4CC9F0',
  text: '#FFFFFF',
  textMuted: '#B3B3C6',
  danger: '#E5484D',
};

export const ANIMATION_DURATION = 0.25;

export const NUMBER_QUESTIONS: QuestionPair[] = [
  { safe: "How many slices of pizza can you eat?", impostor: "How many toddlers could you fight?", type: 'number' },
  { safe: "Rate your driving skills (1-10)", impostor: "Rate your honesty (1-10)", type: 'number' },
  { safe: "What age is 'old'?", impostor: "What age is 'young'?", type: 'number' },
  { safe: "How much ($) for a burger?", impostor: "How much ($) for a kidney?", type: 'number' },
  { safe: "Hours you sleep per night?", impostor: "Hours you work per day?", type: 'number' },
  { safe: "How many countries have you visited?", impostor: "How many times have you been arrested?", type: 'number' },
  { safe: "Rate your cooking (1-10)", impostor: "Rate your singing (1-10)", type: 'number' },
  { safe: "Ideal temperature (Celsius)?", impostor: "Age of your first crush?", type: 'number' },
  { safe: "How many apps on your phone?", impostor: "How many secrets do you have?", type: 'number' },
  { safe: "Desired number of kids?", impostor: "Number of exes?", type: 'number' },
  { safe: "How many pushups can you do?", impostor: "How many lies told today?", type: 'number' },
  { safe: "Rate your style (1-10)", impostor: "Rate your hygiene (1-10)", type: 'number' },
  { safe: "Days without a shower?", impostor: "Weeks without calling mom?", type: 'number' },
  { safe: "How many cups of coffee per day?", impostor: "How many enemies do you have?", type: 'number' },
  { safe: "Year you want to retire?", impostor: "Year you think the world ends?", type: 'number' },
  { safe: "How many siblings do you have?", impostor: "How many people have you ghosted?", type: 'number' },
  { safe: "Rate your patience (1-10)", impostor: "Rate your ego (1-10)", type: 'number' },
  { safe: "How many pets have you owned?", impostor: "How many laws have you broken?", type: 'number' },
  { safe: "Minutes to get ready in the morning?", impostor: "Minutes you'd last in a horror movie?", type: 'number' },
  { safe: "How many books did you read last year?", impostor: "How many times did you cry last month?", type: 'number' },
  { safe: "What is your favorite breakfast food?", impostor: "What is the weirdest thing you've eaten?", type: 'text' },
  { safe: "Where would you go for a dream vacation?", impostor: "Where would you hide a dead body?", type: 'text' },
  { safe: "What is the best job in the world?", impostor: "What is the most dangerous job?", type: 'text' },
  { safe: "Name a movie everyone should watch.", impostor: "Name a movie that made you cry.", type: 'text' },
  { safe: "What is the most useful invention?", impostor: "What is the scariest invention?", type: 'text' }
];

export const WORD_PAIRS: WordPair[] = [
  { safe: "Pizza", impostor: "Burger", category: "Food" },
  { safe: "Coffee", impostor: "Tea", category: "Food" },
  { safe: "Apple", impostor: "Orange", category: "Food" },
  { safe: "Beach", impostor: "Desert", category: "Places" },
  { safe: "London", impostor: "Paris", category: "Places" },
  { safe: "Shark", impostor: "Dolphin", category: "Animals" },
  { safe: "Lion", impostor: "Tiger", category: "Animals" },
  { safe: "Swimming", impostor: "Diving", category: "Activities" },
  { safe: "Running", impostor: "Walking", category: "Activities" },
  { safe: "Pencil", impostor: "Pen", category: "Objects" },
  { safe: "Guitar", impostor: "Violin", category: "Objects" },
  { safe: "Doctor", impostor: "Nurse", category: "Jobs" },
  { safe: "Police", impostor: "Soldier", category: "Jobs" },
  { safe: "Sun", impostor: "Moon", category: "Nature" },
  { safe: "Snow", impostor: "Rain", category: "Nature" }
];
