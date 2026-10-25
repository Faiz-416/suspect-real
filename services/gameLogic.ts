import { Player, QuestionPair, WordPair } from '../types';
import { NUMBER_QUESTIONS, WORD_PAIRS } from '../constants';

export const getRandomQuestion = (): QuestionPair => {
  const index = Math.floor(Math.random() * NUMBER_QUESTIONS.length);
  return NUMBER_QUESTIONS[index];
};

export const getRandomWordPair = (category: string): WordPair => {
  let pool = category === "Random Mix" ? WORD_PAIRS : WORD_PAIRS.filter(p => p.category === category);
  if (pool.length === 0) return WORD_PAIRS[0];
  return pool[Math.floor(Math.random() * pool.length)];
};

export const setupGame1 = (players: Player[], imposterCount: number): Player[] => {
  const cleanPlayers = players.map(p => ({ ...p, isImpostor: false, answer: undefined }));
  const count = Math.max(1, Math.min(imposterCount, Math.floor(players.length / 2)));
  const indices = cleanPlayers.map((_, i) => i).sort(() => Math.random() - 0.5);
  for (let i = 0; i < count; i++) { cleanPlayers[indices[i]].isImpostor = true; }
  return cleanPlayers;
};

export const setupGame2 = (players: Player[], imposterCount: number): Player[] => {
  const p = players.map(pl => ({ ...pl, isImpostor: false }));
  const count = Math.max(1, Math.min(imposterCount, Math.floor(players.length / 2)));
  const indices = p.map((_, i) => i).sort(() => Math.random() - 0.5);
  for (let i = 0; i < count; i++) { p[indices[i]].isImpostor = true; }
  return p;
};
