# WordleBot

A pluggable Wordle solver framework in Java 11 with three competing strategies: entropy-based, heuristic, and minmax algorithms.

## Features

- **Three solver strategies**: Entropy, Heuristic, and Minmax
- **Pluggable architecture**: Easy to implement new solvers via the `Solver` interface
- **No external runtime dependencies**: Only JUnit 5 for testing
- **Maven build**: Compile, test, and package with standard Maven commands
- **Benchmark support**: Built-in harness for multi-run performance analysis

## Quick Start

### Build
```bash
mvn compile
```

### Test
```bash
mvn test
```

### Package
```bash
mvn package
```

## Running a Game

The entry point `Main.java` plays a single game targeting the word `"poppy"`:
```bash
mvn exec:java@run
```

Or via standard Java:
```bash
java -cp target/wordlebot-1.0-SNAPSHOT.jar com.damik3.Main
```

## Testing

### Run all tests
```bash
mvn test
```

### Run specific test class
```bash
mvn -Dtest=WordleTest test
```

### Run single test method
```bash
mvn -Dtest=RulesTest#calculateGuess_shouldWork test
```

### Run solver benchmarks
```bash
# Entropy solver benchmark
mvn -DexcludedGroups= -Dgroups=benchmark -Dtest=com.damik3.solver.entropy.BenchmarkTest test

# Heuristic solver benchmark
mvn -DexcludedGroups= -Dgroups=benchmark -Dtest=com.damik3.solver.heuristic.BenchmarkTest test

# Minmax solver benchmark
mvn -DexcludedGroups= -Dgroups=benchmark -Dtest=com.damik3.solver.minmax.BenchmarkTest test
```

**Note:** `mvn test` excludes `@Tag("benchmark")` tests by default via surefire configuration.

## Architecture

```
Wordle (orchestrator)
├── LoadWords("words.txt")
├── setSolver(Solver)
└── play(solution) → Result {solved, steps, guesses, numPossibleSolutions}

Solver Interface
├── EntropySolver (information-theoretic)
├── HeuristicSolver (score = groups - largestGroup + bias)
└── MinmaxSolver (score = -largestGroup + isPossibleSolution)

Rules (static utilities)
├── eliminateWords(List<String>, List<Guess>)
└── calculateGuess(String guessWord, String solutionWord) → List<Guess>
```

All solver implementations extend `PatternSolver`, which handles:
- First guess selection
- Pattern count generation
- Best guess selection via `calculateScoreByWord()`

## Implementing a Custom Solver

1. Create a class implementing `com.damik3.solver.Solver`:
   ```java
   public class MyCustomSolver implements Solver {
       @Override
       public String firstGuess(List<Word> words) { /* ... */ }

       @Override
       public String nextGuess(List<String> validGuesses, List<String> possibleSolutions) { /* ... */ }
   }
   ```

2. Or extend `PatternSolver` for pattern-based strategies:
   ```java
   public class MyPatternSolver extends PatternSolver {
       @Override
       public void calculateScoreByWord(HashSet<String> possibleSolutions,
           Map<String, Map<List<Guess.Result>, Integer>> patternCountsByWord) {
           // Your scoring logic
       }
   }
   ```

3. Wire it into `Wordle`:
   ```java
   Wordle wordle = new Wordle();
   wordle.loadWords("words.txt");
   wordle.setSolver(new MyCustomSolver());
   Wordle.Result result = wordle.play("slate");
   ```

## Word File Format

The default `words.txt` (11 whitespace-separated columns):
```
word  level  entropyVec(csv)  percentileVec(csv)  expectedEntropy  expectedWordsRemaining
maxWordsRemaining  numberOfGroups  prior  precomputedAverage(a,b)  expectedAdditionalGuesses
```

- `prior > 0` → eligible Wordle answer (included in possible solutions)
- `prior == 0` → guess-only word (used for pattern scoring only)

The first line is a header and is skipped.

## Guess Result Codes

| Enum | toString | Meaning |
|------|----------|---------|
| `CorrectPosition` | `C` | Letter in correct position |
| `WrongPosition` | `W` | Letter in word, wrong position |
| `NotExists` | `_` | Letter not in word |

## Key Files

| File | Purpose |
|------|---------|
| `Wordle.java` | Main orchestrator; loads words, manages game state, delegates to solver |
| `Main.java` | Entry point; plays single game with hardcoded solution |
| `Rules.java` | Static utilities: `eliminateWords()` and `calculateGuess()` |
| `Solver.java` | Strategy interface |
| `PatternSolver.java` | Abstract base for pattern-based solvers |
| `EntropySolver.java` | Entropy-based implementation |
| `HeuristicSolver.java` | Heuristic scoring implementation |
| `MinmaxSolver.java` | Minmax (bottleneck minimization) implementation |
| `Guess.java` | Feedback model with `Result` enum |
| `Word.java` | Parses a single word row from `words.txt` |
| `Benchmark.java` | Benchmark harness for multi-run analysis |

## Testing Structure

- **Unit tests**: `RulesTest`, `PatternSolverTest`, individual solver tests
- **Integration tests**: `WordleTest` (end-to-end game flow)
- **Benchmark tests**: Tagged with `@Tag("benchmark")`, excluded by default
- **Base test class**: `PatternSolverTestBase` provides shared contract tests for all solvers

## Project Metadata

- **Language**: Java 11+
- **Build**: Maven 3.6+
- **Testing**: JUnit Jupiter 5.10.2
- **Plugins**: Maven Compiler 3.11.0, Surefire 3.2.5
- **Dependencies**: None (runtime); JUnit 5 (test-only)

## License

MIT (or as specified in your LICENSE file)

