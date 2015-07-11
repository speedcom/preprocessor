package app

import app.Program2.Preprocessor

object Main2 extends App {

  val (initData, preprocessorInit) = Preprocessor.init("/path/to/file")
//   val x = preprocessorInit.filter // - this is wrong, filter is further step in computation - we have compliation error, very good!
  val (inputData, preprocessorInput) = preprocessorInit.input(initData)
  val (filterData, preprocessorFilter) = preprocessorInput.filter(inputData)
  val (outputData, preprocessorOutput) = preprocessorFilter.output(filterData)

}

object Program2 {

  type Path = String

  sealed trait BasicState
  sealed trait InitState extends BasicState
  sealed trait InputState extends BasicState
  sealed trait FiltrationState extends BasicState
  sealed trait OutputState extends BasicState

  sealed trait Data[D <: Data[D]]
  case class InitData(filePath: Path) extends Data[InitData]
  case class InputData(input: List[String]) extends Data[InputData]
  case class FilterData(filtered: List[String]) extends Data[FilterData]
  case class OutputData(result: String) extends Data[OutputData]

  object Preprocessor {
    val init: Path => (InitData, Preprocessor[InitState]) = {
      path =>
        (InitData(path), new Preprocessor[InitState] {})
    }
  }
  
  class PreprocessorInput extends Preprocessor[InputState]
  class PreprocessorFilter extends Preprocessor[FiltrationState]
  class PreprocessorOutput extends Preprocessor[OutputState]

  trait Preprocessor[+State <: BasicState] {

    def input[T >: State <: InitState]: InitData => (InputData, Preprocessor[InputState]) = {
      data =>
        def fakeReadFile(path: String): List[String] = List("lorem", "ipsum", "dolores")
        val input = fakeReadFile(data.filePath)
        (InputData(input), new PreprocessorInput)
    }
    
    def filter[T >: State <: InputState]: InputData => (FilterData, Preprocessor[FiltrationState]) = {
      data =>
        val filtered = data.input.filter(_.size > 5)
        (FilterData(filtered), new PreprocessorFilter)
    }

    def output[T >: State <: FiltrationState]: FilterData => (OutputData, Preprocessor[OutputState]) = { 
      data =>
        val output = data.filtered.headOption.getOrElse("ERROR - NO RESULT")
        (OutputData(output), new PreprocessorOutput)
    }

  }

}
