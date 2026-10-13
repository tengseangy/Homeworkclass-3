package com.example.data.local

import com.example.model.*
import org.json.JSONArray
import org.json.JSONObject

object WorksheetJsonAdapter {

    fun toJson(worksheet: Worksheet): String {
        val root = JSONObject()
        root.put("id", worksheet.id)
        root.put("titleKhmer", worksheet.titleKhmer)
        root.put("category", worksheet.category.name)
        root.put("gradeLevelKhmer", worksheet.gradeLevelKhmer)
        root.put("subjectKhmer", worksheet.subjectKhmer)
        root.put("chapterNameKhmer", worksheet.chapterNameKhmer)
        root.put("lessonNameKhmer", worksheet.lessonNameKhmer)
        root.put("schoolNameKhmer", worksheet.schoolNameKhmer)
        root.put("teacherNameKhmer", worksheet.teacherNameKhmer)
        root.put("durationMinutes", worksheet.durationMinutes)
        root.put("totalScore", worksheet.totalScore)
        root.put("instructionsKhmer", worksheet.instructionsKhmer)
        root.put("createdAt", worksheet.createdAt)

        val problemsArray = JSONArray()
        for (p in worksheet.problems) {
            val pObj = JSONObject()
            pObj.put("id", p.id)
            pObj.put("lessonId", p.lessonId)
            pObj.put("titleKhmer", p.titleKhmer)
            pObj.put("instructionKhmer", p.instructionKhmer)
            pObj.put("questionKhmer", p.questionKhmer)
            pObj.put("problemType", p.problemType.name)
            pObj.put("points", p.points)
            pObj.put("primaryNumber", p.primaryNumber)
            pObj.put("secondaryNumber", p.secondaryNumber)
            pObj.put("operandA", p.operandA)
            pObj.put("operandB", p.operandB)
            pObj.put("operationSymbol", p.operationSymbol)
            pObj.put("studentAnswerPlaceholderKhmer", p.studentAnswerPlaceholderKhmer)
            pObj.put("teacherAnswerKhmer", p.teacherAnswerKhmer)
            pObj.put("teacherExplanationKhmer", p.teacherExplanationKhmer)
            pObj.put("visualAidType", p.visualAidType ?: "")
            pObj.put("visualAidData", p.visualAidData ?: "")

            val optionsArr = JSONArray()
            for (opt in p.options) {
                val optObj = JSONObject()
                optObj.put("key", opt.key)
                optObj.put("textKhmer", opt.textKhmer)
                optObj.put("isCorrect", opt.isCorrect)
                optionsArr.put(optObj)
            }
            pObj.put("options", optionsArr)

            val stepsArr = JSONArray()
            for (st in p.steps) {
                val stObj = JSONObject()
                stObj.put("stepTitleKhmer", st.stepTitleKhmer)
                stObj.put("explanationKhmer", st.explanationKhmer)
                stObj.put("mathEquationKhmer", st.mathEquationKhmer ?: "")
                stepsArr.put(stObj)
            }
            pObj.put("steps", stepsArr)

            if (p.wordProblem != null) {
                val wpObj = JSONObject()
                wpObj.put("storyKhmer", p.wordProblem.storyKhmer)
                val qArr = JSONArray()
                p.wordProblem.questionsKhmer.forEach { qArr.put(it) }
                wpObj.put("questionsKhmer", qArr)
                val aArr = JSONArray()
                p.wordProblem.answerStatementsKhmer.forEach { aArr.put(it) }
                wpObj.put("answerStatementsKhmer", aArr)
                wpObj.put("calculationFormulaKhmer", p.wordProblem.calculationFormulaKhmer)
                wpObj.put("verificationKhmer", p.wordProblem.verificationKhmer)
                wpObj.put("finalAnswerKhmer", p.wordProblem.finalAnswerKhmer)
                pObj.put("wordProblem", wpObj)
            }

            problemsArray.put(pObj)
        }
        root.put("problems", problemsArray)

        return root.toString()
    }

    fun fromJson(jsonStr: String): Worksheet? {
        return try {
            val root = JSONObject(jsonStr)
            val id = root.optString("id", "")
            val titleKhmer = root.optString("titleKhmer", "")
            val category = try {
                WorksheetCategory.valueOf(root.optString("category", WorksheetCategory.HOMEWORK.name))
            } catch (e: Exception) {
                WorksheetCategory.HOMEWORK
            }
            val gradeLevelKhmer = root.optString("gradeLevelKhmer", "ថ្នាក់ទី ៣")
            val subjectKhmer = root.optString("subjectKhmer", "គណិតវិទ្យា (ផ្នែកចំនួន)")
            val chapterNameKhmer = root.optString("chapterNameKhmer", "")
            val lessonNameKhmer = root.optString("lessonNameKhmer", "")
            val schoolNameKhmer = root.optString("schoolNameKhmer", "សាលាបឋមសិក្សា...")
            val teacherNameKhmer = root.optString("teacherNameKhmer", "លោកគ្រូ / អ្នកគ្រូ...")
            val durationMinutes = root.optInt("durationMinutes", 45)
            val totalScore = root.optInt("totalScore", 100)
            val instructionsKhmer = root.optString("instructionsKhmer", "")
            val createdAt = root.optLong("createdAt", System.currentTimeMillis())

            val problemsList = mutableListOf<MathProblem>()
            val pArray = root.optJSONArray("problems")
            if (pArray != null) {
                for (i in 0 until pArray.length()) {
                    val pObj = pArray.getJSONObject(i)
                    val pId = pObj.optString("id", "")
                    val lessonId = pObj.optString("lessonId", "")
                    val pTitle = pObj.optString("titleKhmer", "")
                    val pInstruction = pObj.optString("instructionKhmer", "")
                    val pQuestion = pObj.optString("questionKhmer", "")
                    val pType = try {
                        ProblemType.valueOf(pObj.optString("problemType", ProblemType.CALCULATION_VERTICAL.name))
                    } catch (e: Exception) {
                        ProblemType.CALCULATION_VERTICAL
                    }
                    val points = pObj.optInt("points", 10)
                    val primaryNumber = pObj.optInt("primaryNumber", 0)
                    val secondaryNumber = pObj.optInt("secondaryNumber", 0)
                    val operandA = pObj.optString("operandA", "")
                    val operandB = pObj.optString("operandB", "")
                    val opSymbol = pObj.optString("operationSymbol", "")
                    val placeholder = pObj.optString("studentAnswerPlaceholderKhmer", "ចម្លើយ៖ ...")
                    val teacherAnswer = pObj.optString("teacherAnswerKhmer", "")
                    val teacherExp = pObj.optString("teacherExplanationKhmer", "")
                    val visualType = pObj.optString("visualAidType").ifEmpty { null }
                    val visualData = pObj.optString("visualAidData").ifEmpty { null }

                    val optsList = mutableListOf<ProblemOption>()
                    val optsArr = pObj.optJSONArray("options")
                    if (optsArr != null) {
                        for (j in 0 until optsArr.length()) {
                            val opt = optsArr.getJSONObject(j)
                            optsList.add(
                                ProblemOption(
                                    key = opt.optString("key", ""),
                                    textKhmer = opt.optString("textKhmer", ""),
                                    isCorrect = opt.optBoolean("isCorrect", false)
                                )
                            )
                        }
                    }

                    val stepsList = mutableListOf<SolutionStep>()
                    val stArr = pObj.optJSONArray("steps")
                    if (stArr != null) {
                        for (k in 0 until stArr.length()) {
                            val st = stArr.getJSONObject(k)
                            stepsList.add(
                                SolutionStep(
                                    stepTitleKhmer = st.optString("stepTitleKhmer", ""),
                                    explanationKhmer = st.optString("explanationKhmer", ""),
                                    mathEquationKhmer = st.optString("mathEquationKhmer").ifEmpty { null }
                                )
                            )
                        }
                    }

                    var wordProblem: WordProblemDetail? = null
                    val wpObj = pObj.optJSONObject("wordProblem")
                    if (wpObj != null) {
                        val qList = mutableListOf<String>()
                        val qArr = wpObj.optJSONArray("questionsKhmer")
                        if (qArr != null) {
                            for (q in 0 until qArr.length()) qList.add(qArr.getString(q))
                        }
                        val aList = mutableListOf<String>()
                        val aArr = wpObj.optJSONArray("answerStatementsKhmer")
                        if (aArr != null) {
                            for (a in 0 until aArr.length()) aList.add(aArr.getString(a))
                        }

                        wordProblem = WordProblemDetail(
                            storyKhmer = wpObj.optString("storyKhmer", ""),
                            questionsKhmer = qList,
                            answerStatementsKhmer = aList,
                            calculationFormulaKhmer = wpObj.optString("calculationFormulaKhmer", ""),
                            verificationKhmer = wpObj.optString("verificationKhmer", ""),
                            finalAnswerKhmer = wpObj.optString("finalAnswerKhmer", "")
                        )
                    }

                    problemsList.add(
                        MathProblem(
                            id = pId,
                            lessonId = lessonId,
                            titleKhmer = pTitle,
                            instructionKhmer = pInstruction,
                            questionKhmer = pQuestion,
                            problemType = pType,
                            points = points,
                            options = optsList,
                            primaryNumber = primaryNumber,
                            secondaryNumber = secondaryNumber,
                            operandA = operandA,
                            operandB = operandB,
                            operationSymbol = opSymbol,
                            studentAnswerPlaceholderKhmer = placeholder,
                            teacherAnswerKhmer = teacherAnswer,
                            teacherExplanationKhmer = teacherExp,
                            steps = stepsList,
                            wordProblem = wordProblem,
                            visualAidType = visualType,
                            visualAidData = visualData
                        )
                    )
                }
            }

            Worksheet(
                id = id,
                titleKhmer = titleKhmer,
                category = category,
                gradeLevelKhmer = gradeLevelKhmer,
                subjectKhmer = subjectKhmer,
                chapterNameKhmer = chapterNameKhmer,
                lessonNameKhmer = lessonNameKhmer,
                schoolNameKhmer = schoolNameKhmer,
                teacherNameKhmer = teacherNameKhmer,
                durationMinutes = durationMinutes,
                totalScore = totalScore,
                instructionsKhmer = instructionsKhmer,
                problems = problemsList,
                createdAt = createdAt
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
