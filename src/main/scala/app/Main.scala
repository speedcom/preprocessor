package app

import app.Program.Preprocessor

object Main extends App {

  Preprocessor.init.input.filter.output
//  another sequention of computation is just wrong - this is guaranteed on compilation step (not runtime!)
//  Preprocessor.init.output - // compilation error
//  Preprocessor.init.filter - // compilation error
//  Preprocessor.init.input.output - // compilation error
}

object Program {
  
  sealed trait BasicState
  sealed trait InitState extends BasicState
  sealed trait InputState extends BasicState
  sealed trait FiltrationState extends BasicState
  sealed trait OutputState extends BasicState

  object Preprocessor {
    val init = new Preprocessor[InitState] {}
  }

  class PreprocessorInput extends Preprocessor[InputState]
  class PreprocessorFilter extends Preprocessor[FiltrationState]
  class PreprocessorOutput extends Preprocessor[OutputState]

  trait Preprocessor[+State <: BasicState] {

    def input[T >: State <: InitState]: Preprocessor[InputState] = new PreprocessorInput
    def filter[T >: State <: InputState]: Preprocessor[FiltrationState] = new PreprocessorFilter
    def output[T >: State <: FiltrationState]: Preprocessor[OutputState] = new PreprocessorOutput

  }

}
