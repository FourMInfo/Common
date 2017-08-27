import scala.util.Try
import scala.util.{Success,Failure}

import $ivy.`com.github.nscala-time::nscala-time:2.16.0`
import com.github.nscala_time.time.Imports._

def theDate = DateTime.now.toLocalDateTime.toString

// use touch to either create log if it doesnt exist, or do a harmless non-action
def makeLog = {
    val logFile = Path("logfile.log",pwd)
    %%('touch,logFile)
    val headDay = theDate
    write.append(logFile,s"Log that follows written at $headDay\n" )
    logFile
}

// function to process the result of try, log message and return whether to continue or not
def contOrNot(cR: Try[CommandResult], msg:String, logFile:Path) : Boolean = {
    val cRS = 
        cR match {
            case Success(cR) => cR.out.string
            case Failure(cR) => cR.getMessage
        }
    if (cR.isSuccess) {
        write.append(logFile,s"Successfully $msg with message $cRS\n")
        true
    } else {
        write.append(logFile,s"Failed to $msg with message $cRS\n")
        false
    }
}
