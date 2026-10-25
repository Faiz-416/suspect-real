import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { LucidePlus, LucideX, LucideHash, LucidePlay, LucidePause, LucideRefreshCcw, LucideCheck, LucideMinus, LucideTimer, LucideUsers, LucideArrowRight, LucideInfo, LucideLock, LucideSearch } from 'lucide-react';
import { AppScreen, GameMode, GameState, Player, CATEGORIES } from './types';
import { setupGame1, setupGame2, getRandomQuestion, getRandomWordPair } from './services/gameLogic';
import { Button, Card, Heading, SubHeading, ScreenContainer, Input, ConfirmationModal, InfoModal } from './components/UI';
import { ANIMATION_DURATION } from './constants';

const formatName = (s: string) => {
  if (!s) return '';
  const trimmed = s.trim();
  if (!trimmed) return '';
  return trimmed.charAt(0).toUpperCase() + trimmed.slice(1);
};

const ScreenHome: React.FC<{ onSelect: (mode: GameMode) => void }> = ({ onSelect }) => (
  <div className="flex flex-col gap-4 justify-center h-full">
      <div className="text-center mb-8">
          <Heading>Select Game</Heading>
          <SubHeading>Choose what you want to play</SubHeading>
      </div>
      
      <Card onClick={() => onSelect(GameMode.NUMBER_JUSTIFY)} className="cursor-pointer active:scale-95 transition-transform flex flex-col justify-between h-48 border border-white/5 hover:border-[#7C5CFF]/50">
          <div className="p-3 bg-[#7C5CFF]/10 rounded-xl text-[#7C5CFF] w-fit mb-4">
              <LucideHash size={32} />
          </div>
          <div>
              <h3 className="text-2xl font-bold mb-1 text-white">Number Justify</h3>
              <p className="text-sm text-[#B3B3C6] font-medium">Justify your answer. Find the liar.</p>
          </div>
      </Card>

      <Card onClick={() => onSelect(GameMode.IMPOSTOR_WORD)} className="cursor-pointer active:scale-95 transition-transform flex flex-col justify-between h-48 border border-white/5 hover:border-[#4CC9F0]/50">
          <div className="p-3 bg-[#4CC9F0]/10 rounded-xl text-[#4CC9F0] w-fit mb-4">
              <LucideSearch size={32} />
          </div>
          <div>
              <h3 className="text-2xl font-bold mb-1 text-white">Impostor Word</h3>
              <p className="text-sm text-[#B3B3C6] font-medium">Find the player with the different word.</p>
          </div>
      </Card>
  </div>
);

const ScreenPlayerSetup: React.FC<{ 
  players: Player[]; 
  roundDuration: number;
  imposterCount: number;
  onAdd: (name: string) => void; 
  onRemove: (id: string) => void; 
  onStart: () => void; 
  onUpdateDuration: (newDuration: number) => void;
  onUpdateImposters: (count: number) => void;
  onOpenInfo: () => void;
}> = ({ players, roundDuration, imposterCount, onAdd, onRemove, onStart, onUpdateDuration, onUpdateImposters, onOpenInfo }) => {
  const [name, setName] = useState('');

  const handleSubmit = () => {
    if (name.trim()) {
      onAdd(name);
      setName('');
    }
  };

  const formatDuration = (sec: number) => {
    const m = Math.floor(sec / 60);
    const s = sec % 60;
    return `${m}:${s.toString().padStart(2, '0')}`;
  };

  const maxImposters = Math.max(1, Math.floor(players.length / 2));

  return (
    <div className="flex flex-col h-full pt-16">
        <div className="flex justify-between items-center mb-6">
            <Heading className="mb-0">Players</Heading>
            <button 
                onClick={onOpenInfo}
                className="w-10 h-10 rounded-full border border-white/10 flex items-center justify-center text-[#B3B3C6] hover:bg-white/5 active:scale-95 transition-all"
            >
                <LucideInfo size={20} />
            </button>
        </div>

        <div className="flex gap-2 mb-6">
            <Input 
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Enter player name"
                onKeyDown={(e) => e.key === 'Enter' && handleSubmit()}
            />
            <button 
                onClick={handleSubmit}
                className="bg-[#7C5CFF] text-white h-auto px-6 rounded-[20px] font-bold shadow-lg active:scale-95 transition-transform"
            >
                <LucidePlus size={28} />
            </button>
        </div>

        <div className="flex-1 overflow-y-auto no-scrollbar content-start flex flex-wrap gap-3 pb-4">
            <AnimatePresence>
                {players.map(p => (
                    <motion.div
                        key={p.id}
                        initial={{ scale: 0, opacity: 0 }}
                        animate={{ scale: 1, opacity: 1 }}
                        exit={{ scale: 0, opacity: 0 }}
                        className="bg-[#1A1A22] pl-5 pr-2 py-3 rounded-full flex items-center gap-3 border border-white/5"
                    >
                        <span className="font-bold text-white text-lg">{p.name}</span>
                        <button onClick={() => onRemove(p.id)} className="p-2 hover:bg-white/10 text-[#B3B3C6] rounded-full">
                            <LucideX size={14} />
                        </button>
                    </motion.div>
                ))}
            </AnimatePresence>
            {players.length === 0 && (
                <div className="w-full text-center text-[#B3B3C6] opacity-50 mt-10 font-medium">Add at least 3 players</div>
            )}
        </div>

        <div className="flex gap-3 mb-6">
            <Card className="!py-4 flex-1 flex flex-col items-center justify-center gap-2">
                <div className="flex items-center gap-2 text-[#B3B3C6] text-xs uppercase font-bold">
                    <LucideTimer size={14} /> Time
                </div>
                <div className="flex items-center gap-3">
                   <button onClick={() => onUpdateDuration(Math.max(30, roundDuration - 30))} className="p-1 bg-[#2A2A35] rounded-full text-[#B3B3C6] active:bg-[#7C5CFF] active:text-white"><LucideMinus size={14} /></button>
                   <span className="font-bold text-lg text-white">{formatDuration(roundDuration)}</span>
                   <button onClick={() => onUpdateDuration(Math.min(300, roundDuration + 30))} className="p-1 bg-[#2A2A35] rounded-full text-[#B3B3C6] active:bg-[#7C5CFF] active:text-white"><LucidePlus size={14} /></button>
                </div>
            </Card>

            <Card className="!py-4 flex-1 flex flex-col items-center justify-center gap-2">
                <div className="flex items-center gap-2 text-[#B3B3C6] text-xs uppercase font-bold">
                    <LucideUsers size={14} /> Imposters
                </div>
                <div className="flex items-center gap-3">
                   <button onClick={() => onUpdateImposters(Math.max(1, imposterCount - 1))} className="p-1 bg-[#2A2A35] rounded-full text-[#B3B3C6] active:bg-[#7C5CFF] active:text-white"><LucideMinus size={14} /></button>
                   <span className="font-bold text-lg text-white">{Math.min(imposterCount, maxImposters)}</span>
                   <button onClick={() => onUpdateImposters(Math.min(maxImposters, imposterCount + 1))} className="p-1 bg-[#2A2A35] rounded-full text-[#B3B3C6] active:bg-[#7C5CFF] active:text-white"><LucidePlus size={14} /></button>
                </div>
            </Card>
        </div>

        <Button 
            disabled={players.length < 3} 
            onClick={onStart}
        >
            Start Mission ({players.length})
        </Button>
    </div>
  );
};

const ScreenG1Assignment: React.FC<{
  player: Player;
  questionSafe: string;
  questionImpostor: string;
  onContinue: () => void;
}> = ({ player, questionSafe, questionImpostor, onContinue }) => {
  const [revealed, setRevealed] = useState(false);

  return (
    <div className="h-full flex flex-col justify-center items-center text-center pt-10">
        <SubHeading>Pass device to</SubHeading>
        <Heading className="text-5xl mb-12 text-white">{player?.name}</Heading>
        
        <AnimatePresence mode='wait'>
            {!revealed ? (
                <motion.div 
                    key="ask"
                    initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -20 }}
                    className="w-full"
                >
                    <Button onClick={() => setRevealed(true)} variant="secondary">I am {player?.name}</Button>
                </motion.div>
            ) : (
                <motion.div
                    key="content"
                    initial={{ opacity: 0, scale: 0.9 }} animate={{ opacity: 1, scale: 1 }}
                    className="w-full"
                >
                    <Card className="mb-8 py-10 bg-[#1A1A22] border border-[#7C5CFF]/20">
                        <h3 className="text-[#7C5CFF] text-xs font-black uppercase mb-4 tracking-[0.2em]">Your Question</h3>
                        <p className="text-2xl font-bold leading-relaxed text-white">
                            {player?.isImpostor ? questionImpostor : questionSafe}
                        </p>
                    </Card>
                    <Button onClick={onContinue}>
                        Answer Now
                    </Button>
                </motion.div>
            )}
        </AnimatePresence>
    </div>
  );
};

const ScreenG1Input: React.FC<{
  player: Player;
  onSubmit: (ans: string) => void;
}> = ({ player, onSubmit }) => {
  const [val, setVal] = useState('');

  return (
    <div className="h-full flex flex-col pt-16">
         <div className="flex items-center gap-4 mb-8">
             <div className="flex-1">
                 <p className="text-sm text-[#B3B3C6]">Answering as</p>
                 <h2 className="text-3xl font-bold text-white">{player?.name}</h2>
             </div>
         </div>

         <div className="flex-1 flex flex-col justify-center items-center">
             <input 
                type="text" 
                autoFocus
                value={val}
                onChange={(e) => setVal(e.target.value)}
                className="w-full bg-transparent text-center text-5xl font-black text-white border-b-2 border-[#1A1A22] focus:border-[#7C5CFF] outline-none pb-4 mb-8 placeholder-[#1A1A22]"
                placeholder="..."
             />
             <p className="text-center text-[#B3B3C6] max-w-xs font-medium">
                 Enter a value that answers your question honestly.
             </p>
         </div>

         <Button 
            onClick={() => val && onSubmit(val)} 
            disabled={!val}
         >
             Confirm Answer
         </Button>
    </div>
  );
};

const ScreenG1Reveal: React.FC<{
  players: Player[];
  onNext: () => void;
}> = ({ players, onNext }) => (
  <div className="h-full flex flex-col pt-10">
      <Heading className="text-center mb-8">All Answers</Heading>
      
      <div className="grid grid-cols-2 gap-4 content-start">
          {players.map((p, i) => (
              <motion.div 
                key={p.id}
                initial={{ rotateX: 90, opacity: 0 }}
                animate={{ rotateX: 0, opacity: 1 }}
                transition={{ delay: i * 0.1, type: "spring" }}
              >
                  <Card className="flex flex-col items-center justify-center py-6 px-2 bg-[#1A1A22] border border-white/5">
                      <div className="text-sm font-bold text-[#B3B3C6] mb-2">{p.name}</div>
                      <div className="text-3xl font-black text-[#7C5CFF]">{p.answer ?? p.numberAnswer}</div>
                  </Card>
              </motion.div>
          ))}
      </div>

      <div className="mt-auto pt-6">
        <Button onClick={onNext}>
            Start Discussion
        </Button>
      </div>
  </div>
);

const ScreenDiscussion: React.FC<{
  timeLeft: number;
  totalTime: number;
  isRunning: boolean;
  onToggleTimer: () => void;
  onVote: () => void;
  question: string;
  players: Player[];
  gameMode: GameMode | null;
}> = ({ timeLeft, isRunning, onToggleTimer, onVote, question, players, gameMode }) => {
  const isLowTime = timeLeft <= 10 && timeLeft > 0;
  
  return (
    <div className="h-full flex flex-col pt-16 overflow-hidden">
        <Heading className="text-center">Discussion</Heading>
        
        <div className="flex-1 flex flex-col overflow-y-auto no-scrollbar pt-2">
            <Card className="mb-4 bg-[#7C5CFF]/10 border border-[#7C5CFF]/20 !p-4">
                <p className="text-center text-[#7C5CFF] font-bold text-lg leading-snug">"{question}"</p>
            </Card>

            {gameMode === GameMode.NUMBER_JUSTIFY && (
                <div className="grid grid-cols-3 gap-2 mb-4">
                    {players.map(p => (
                        <div key={p.id} className="bg-[#1A1A22] border border-white/5 rounded-xl p-2 text-center">
                            <div className="text-[10px] font-bold text-[#B3B3C6] truncate uppercase mb-1">{p.name}</div>
                            <div className="text-xl font-black text-[#7C5CFF] leading-tight break-words">{p.answer ?? p.numberAnswer}</div>
                        </div>
                    ))}
                </div>
            )}

            <div className="flex-1 flex items-center justify-center min-h-[140px]">
                <div className={`text-8xl font-black font-mono tracking-tighter ${isLowTime ? 'text-[#E5484D]' : 'text-white'}`}>
                    {Math.floor(timeLeft / 60)}:{(timeLeft % 60).toString().padStart(2, '0')}
                </div>
            </div>
        </div>

        <div className="flex gap-4 mb-6 mt-4">
            <Button 
              variant="secondary" 
              onClick={onToggleTimer}
              className="flex-1 h-[60px]"
            >
                {isRunning ? <LucidePause /> : <LucidePlay />}
            </Button>
            <Button 
              className="flex-[2] h-[60px]"
              onClick={onVote}
            >
                Vote Now
            </Button>
        </div>
    </div>
  );
};

const ScreenG2CategorySelect: React.FC<{
  onSelect: (category: string) => void;
}> = ({ onSelect }) => (
  <div className="h-full flex flex-col pt-16">
      <Heading>Select Category</Heading>
      <div className="flex-1 overflow-y-auto no-scrollbar grid grid-cols-2 gap-3 py-4 content-start">
          {CATEGORIES.map((cat) => (
              <Card key={cat} onClick={() => onSelect(cat)} className="cursor-pointer active:scale-95 transition-transform border border-white/5 hover:border-[#4CC9F0]/30 flex items-center justify-center text-center !p-4 !min-h-[100px]">
                  <h3 className="font-bold text-lg text-white">{cat}</h3>
              </Card>
          ))}
      </div>
  </div>
);

const ScreenG2RevealFlow: React.FC<{
  player: Player;
  wordSafe: string;
  wordImpostor: string;
  onDone: () => void;
}> = ({ player, wordSafe, wordImpostor, onDone }) => {
  const [isHolding, setIsHolding] = useState(false);
  const [progress, setProgress] = useState(0);
  const [showWord, setShowWord] = useState(false);
  const [hasRevealed, setHasRevealed] = useState(false);

  useEffect(() => {
    let rafId: number;
    if (isHolding) {
      const startTime = Date.now();
      const animate = () => {
        const p = Math.min(100, ((Date.now() - startTime) / 1700) * 100);
        setProgress(p);
        if (p >= 100) { setShowWord(true); setHasRevealed(true); }
        if (isHolding && p < 100) rafId = requestAnimationFrame(animate);
      };
      rafId = requestAnimationFrame(animate);
    } else { setProgress(0); setShowWord(false); }
    return () => cancelAnimationFrame(rafId);
  }, [isHolding]);

  const displayWord = player?.isImpostor ? wordImpostor : wordSafe;
  const secondsLeft = Math.ceil(2.5 - (progress / 100 * 2.5));

  return (
      <div className="h-full flex flex-col pt-10 pb-6 overflow-hidden relative select-none">
            <div className="flex-1 flex flex-col items-center justify-center pt-6">
                <Heading className="text-4xl mb-2">{player?.name}</Heading>
                <p className="text-[#B3B3C6] font-medium mb-12">Guard the scrolls! The spies are among us!</p>
                <button
                    className="relative w-64 h-64 rounded-full bg-[#1A1A22] border-4 border-[#7C5CFF]/30 flex flex-col items-center justify-center overflow-hidden touch-none"
                    onPointerDown={() => setIsHolding(true)}
                    onPointerUp={() => setIsHolding(false)}
                    onPointerLeave={() => setIsHolding(false)}
                >
                    <div className="absolute bottom-0 left-0 right-0 bg-[#7C5CFF] opacity-30 transition-all ease-linear" style={{ height: `${progress}%` }} />
                    <div className="z-10 text-center px-4">
                        {showWord ? (
                            <motion.div initial={{ opacity: 0, scale: 0.8 }} animate={{ opacity: 1, scale: 1 }}>
                                <p className="uppercase tracking-widest text-[10px] font-bold text-[#4CC9F0] mb-2">Your Word</p>
                                <h2 className="text-3xl font-black text-white">{displayWord}</h2>
                            </motion.div>
                        ) : (
                            <>{isHolding ? <div className="text-6xl font-black text-white">{secondsLeft}</div> : <><LucideLock size={40} className="mb-4 mx-auto text-[#B3B3C6]" /><h3 className="text-lg font-bold">Hold to Reveal</h3></>}</>
                        )}
                    </div>
                </button>
            </div>
            {hasRevealed && <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="px-5 mt-auto"><Button onClick={onDone} variant="secondary">Continue</Button></motion.div>}
      </div>
  );
};

const ScreenG2PassPhone: React.FC<{
  nextPlayerName: string;
  onContinue: () => void;
}> = ({ nextPlayerName, onContinue }) => (
  <div className="h-full flex flex-col items-center justify-center text-center p-6">
     <div className="w-24 h-24 bg-[#1A1A22] rounded-full flex items-center justify-center mb-6">
        <LucideArrowRight size={48} className="text-[#4CC9F0]" />
     </div>
     <SubHeading>Pass the phone to</SubHeading>
     <Heading className="text-5xl mb-12">{nextPlayerName}</Heading>
     <Button onClick={onContinue}>Continue</Button>
  </div>
);

const ScreenResults: React.FC<any> = ({ players, votes, gameMode, qImpostor, qSafe, wImpostor, wSafe, onPlayAgain, onChangePlayers }) => {
  const impostors = players.filter((p: Player) => p.isImpostor);
  const groupWon = players.find((p: Player) => p.id === votes['GROUP_DECISION'])?.isImpostor;

  return (
      <div className="h-full flex flex-col justify-center items-center text-center pt-10">
           <div className={`rounded-full px-8 py-3 text-2xl font-black shadow-lg border-4 ${groupWon ? 'bg-[#7C5CFF] border-[#4CC9F0]' : 'bg-[#E5484D] border-white'}`}>
                {groupWon ? "CAUGHT!" : "ESCAPED!"}
           </div>
           <div className="w-full flex-1 overflow-y-auto no-scrollbar pt-8">
               <Heading className="text-5xl mb-4">{groupWon ? "PLAYERS WIN" : "IMPOSTORS WIN"}</Heading>
               <p className="text-[#B3B3C6] mb-4 font-bold uppercase tracking-widest">Minority Group (Impostors):</p>
               <div className="flex justify-center gap-2 mb-8 flex-wrap">
                 {impostors.map((imp: Player) => <span key={imp.id} className="bg-[#E5484D] px-4 py-2 rounded-lg font-bold">{imp.name}</span>)}
               </div>
               <Card className="text-left w-full mb-8 bg-[#1A1A22] border border-white/5">
                   <div className="mb-6"><p className="text-xs font-bold text-[#E5484D] uppercase">Impostor Secret</p><p className="font-bold text-xl">{gameMode === GameMode.NUMBER_JUSTIFY ? qImpostor : wImpostor}</p></div>
                   <div><p className="text-xs font-bold text-[#4CC9F0] uppercase">Player Secret</p><p className="font-bold text-xl">{gameMode === GameMode.NUMBER_JUSTIFY ? qSafe : wSafe}</p></div>
               </Card>
               <div className="flex gap-3 mt-auto pb-6">
                   <Button onClick={onChangePlayers} variant="secondary" className="flex-1">Change Players</Button>
                   <Button onClick={onPlayAgain} className="flex-[2]"><LucideRefreshCcw className="mr-2 inline" size={20} /> Play Again</Button>
               </div>
           </div>
      </div>
  );
};

const ScreenVote: React.FC<any> = ({ players, votes, onToggleVote, onConfirm }) => (
  <div className="h-full flex flex-col pt-10">
      <Heading className="text-center mb-2">Who is the Impostor?</Heading>
      <div className="grid grid-cols-2 gap-4 mt-8">
          {players.map((p: Player) => {
              const isSelected = votes['GROUP_DECISION'] === p.id;
              return (
                <Card 
                  key={p.id} 
                  onClick={() => onToggleVote(p.id)} 
                  className={`relative flex flex-col items-center py-6 border-4 transition-all ${isSelected ? 'border-[#E5484D] bg-[#2A1A1A]' : 'border-transparent'}`}
                >
                    <div className="font-bold text-xl text-white">{p.name}</div>
                    {isSelected && (
                      <div className="absolute top-2 right-2 bg-[#E5484D] rounded-full p-1">
                        <LucideCheck size={16} />
                      </div>
                    )}
                </Card>
              );
          })}
      </div>
      <Button onClick={onConfirm} disabled={!votes['GROUP_DECISION']} className="mt-auto" variant="danger">Lock In Vote</Button>
  </div>
);

const App = () => {
  const [state, setState] = useState<GameState>({
    screen: AppScreen.HOME,
    players: [],
    activeGame: null,
    imposterCount: 1,
    category: "Random Mix",
    currentPlayerIndex: 0,
    questionSafe: '',
    questionImpostor: '',
    questionType: 'number',
    wordSafe: '',
    wordImpostor: '',
    timeLeft: 0,
    roundDuration: 120, 
    isTimerRunning: false,
    votes: {}
  });

  const [showExitConfirm, setShowExitConfirm] = useState(false);
  const [showInfoModal, setShowInfoModal] = useState(false);

  useEffect(() => {
    let interval: any;
    if (state.isTimerRunning && state.timeLeft > 0) {
      interval = setInterval(() => setState(prev => ({ ...prev, timeLeft: prev.timeLeft - 1 })), 1000);
    }
    return () => clearInterval(interval);
  }, [state.isTimerRunning, state.timeLeft]);

  const addPlayer = (name: string) => {
    const capName = formatName(name);
    if (!capName || state.players.some(p => p.name === capName)) return;
    setState(prev => ({ ...prev, players: [...prev.players, { id: crypto.randomUUID(), name: capName, avatarSeed: 0, isImpostor: false, score: 0 }] }));
  };

  const proceedToGameSetup = () => {
    if (state.activeGame === GameMode.NUMBER_JUSTIFY) {
      const q = getRandomQuestion();
      setState(prev => ({ 
          ...prev, 
          players: setupGame1(prev.players, prev.imposterCount), 
          questionSafe: q.safe, 
          questionImpostor: q.impostor, 
          currentPlayerIndex: 0, 
          screen: AppScreen.G1_ASSIGNMENT 
      }));
    } else { setState(prev => ({ ...prev, screen: AppScreen.G2_CATEGORY_SELECT })); }
  };

  const selectCategory = (cat: string) => {
    const w = getRandomWordPair(cat);
    setState(prev => ({ ...prev, category: cat, players: setupGame2(prev.players, prev.imposterCount), wordSafe: w.safe, wordImpostor: w.impostor, currentPlayerIndex: 0, screen: AppScreen.G2_REVEAL_FLOW }));
  };

  const submitG1Answer = (ans: string) => {
      const updated = [...state.players];
      updated[state.currentPlayerIndex].answer = ans;
      const num = parseInt(ans);
      if (!isNaN(num)) updated[state.currentPlayerIndex].numberAnswer = num;
      
      const isLast = state.currentPlayerIndex === state.players.length - 1;
      setState(prev => ({ ...prev, players: updated, currentPlayerIndex: isLast ? 0 : prev.currentPlayerIndex + 1, screen: isLast ? AppScreen.G1_REVEAL : AppScreen.G1_ASSIGNMENT }));
  };

  const handleBack = () => {
    if (state.screen === AppScreen.HOME) return;
    if (state.screen === AppScreen.PLAYER_SETUP) setState(p => ({...p, screen: AppScreen.HOME}));
    else setShowExitConfirm(true);
  };

  return (
    <ScreenContainer onBack={state.screen !== AppScreen.HOME ? handleBack : undefined}>
        <ConfirmationModal isOpen={showExitConfirm} title="Abort Mission?" message="Leaving now will cancel the current game and lose all progress." onConfirm={() => {setShowExitConfirm(false); setState(p=>({...p, screen: AppScreen.HOME}))}} onCancel={() => setShowExitConfirm(false)} />
        <InfoModal isOpen={showInfoModal} onClose={() => setShowInfoModal(false)} />
        <AnimatePresence mode='wait'>
            <motion.div key={state.screen} initial={{ opacity: 0, x: 20 }} animate={{ opacity: 1, x: 0 }} exit={{ opacity: 0, x: -20 }} transition={{ duration: ANIMATION_DURATION }} className="h-full w-full">
                {state.screen === AppScreen.HOME && <ScreenHome onSelect={(m) => setState(p=>({...p, activeGame:m, screen:AppScreen.PLAYER_SETUP}))} />}
                {state.screen === AppScreen.PLAYER_SETUP && <ScreenPlayerSetup players={state.players} roundDuration={state.roundDuration} imposterCount={state.imposterCount} onAdd={addPlayer} onRemove={(id:string)=>setState(p=>({...p, players:p.players.filter(pl=>pl.id!==id)}))} onStart={proceedToGameSetup} onUpdateDuration={(d:number)=>setState(p=>({...p, roundDuration:d}))} onUpdateImposters={(c:number)=>setState(p=>({...p, imposterCount:c}))} onOpenInfo={()=>setShowInfoModal(true)} />}
                
                {state.screen === AppScreen.G1_ASSIGNMENT && <ScreenG1Assignment player={state.players[state.currentPlayerIndex]} questionSafe={state.questionSafe} questionImpostor={state.questionImpostor} onContinue={()=>setState(p=>({...p, screen:AppScreen.G1_INPUT}))} />}
                {state.screen === AppScreen.G1_INPUT && <ScreenG1Input player={state.players[state.currentPlayerIndex]} onSubmit={submitG1Answer} />}
                {state.screen === AppScreen.G1_REVEAL && <ScreenG1Reveal players={state.players} onNext={()=>setState(p=>({...p, timeLeft:p.roundDuration, isTimerRunning:true, screen:AppScreen.G1_DISCUSS}))} />}
                
                {state.screen === AppScreen.G2_CATEGORY_SELECT && <ScreenG2CategorySelect onSelect={selectCategory} />}
                {state.screen === AppScreen.G2_REVEAL_FLOW && <ScreenG2RevealFlow player={state.players[state.currentPlayerIndex]} wordSafe={state.wordSafe} wordImpostor={state.wordImpostor} onDone={()=>setState(p=>({...p, screen:AppScreen.G2_PASS_PHONE}))} />}
                {state.screen === AppScreen.G2_PASS_PHONE && <ScreenG2PassPhone nextPlayerName={state.currentPlayerIndex === state.players.length - 1 ? "Everyone" : state.players[state.currentPlayerIndex + 1]?.name} onContinue={() => { const isLast = state.currentPlayerIndex === state.players.length - 1; if (isLast) setState(p=>({...p, timeLeft:p.roundDuration, isTimerRunning:true, screen:AppScreen.G2_GAMEPLAY})); else setState(p=>({...p, currentPlayerIndex: p.currentPlayerIndex + 1, screen: AppScreen.G2_REVEAL_FLOW})) }} />}

                {(state.screen === AppScreen.G1_DISCUSS || state.screen === AppScreen.G2_GAMEPLAY) && <ScreenDiscussion timeLeft={state.timeLeft} totalTime={state.roundDuration} isRunning={state.isTimerRunning} onToggleTimer={()=>setState(p=>({...p, isTimerRunning:!p.isTimerRunning}))} onVote={()=>setState(p=>({...p, screen: state.activeGame === GameMode.NUMBER_JUSTIFY ? AppScreen.G1_VOTE : AppScreen.G2_VOTE}))} question={state.activeGame === GameMode.NUMBER_JUSTIFY ? state.questionSafe : `Category: ${state.category}`} players={state.players} gameMode={state.activeGame} />}
                
                {(state.screen === AppScreen.G1_VOTE || state.screen === AppScreen.G2_VOTE) && <ScreenVote players={state.players} votes={state.votes} onToggleVote={(id:string)=>setState(p=>({...p, votes: {'GROUP_DECISION': id}}))} onConfirm={()=>setState(p=>({...p, screen: state.activeGame === GameMode.NUMBER_JUSTIFY ? AppScreen.G1_RESULTS : AppScreen.G2_RESULTS}))} />}
                {(state.screen === AppScreen.G1_RESULTS || state.screen === AppScreen.G2_RESULTS) && <ScreenResults players={state.players} votes={state.votes} gameMode={state.activeGame} qImpostor={state.questionImpostor} qSafe={state.questionSafe} wImpostor={state.wordImpostor} wSafe={state.wordSafe} onPlayAgain={() => { proceedToGameSetup(); setState(p=>({...p, votes:{}, isTimerRunning:false})) }} onChangePlayers={() => setState(p=>({...p, screen: AppScreen.PLAYER_SETUP, votes:{}, isTimerRunning:false}))} />}
            </motion.div>
        </AnimatePresence>
    </ScreenContainer>
  );
};

export default App;
