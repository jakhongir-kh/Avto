package com.uztest.avto.data.repository

import com.uztest.avto.domain.model.Category
import com.uztest.avto.domain.model.Question
import com.uztest.avto.domain.repository.QuestionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepositoryImpl @Inject constructor() : QuestionRepository {
    
    private val categories = listOf(
        Category(1, "Traffic Signs", "Learn about road signs and their meanings", "traffic_light", 50),
        Category(2, "Road Rules", "Basic traffic rules and regulations", "rule", 45),
        Category(3, "Safety", "Vehicle safety and emergency procedures", "security", 40),
        Category(4, "Parking", "Parking rules and regulations", "local_parking", 35),
        Category(5, "Intersections", "Navigation through intersections", "intersection", 30),
        Category(6, "Highway Driving", "Highway rules and safe driving", "highway", 25)
    )
    
    private val questions = listOf(
        // Traffic Signs
        Question(1, "What does a red octagonal sign mean?", 
            listOf("Yield", "Stop", "Caution", "No Entry"), 1, categories[0]),
        Question(2, "What does a yellow diamond sign typically indicate?", 
            listOf("Warning", "Information", "Prohibition", "Direction"), 0, categories[0]),
        Question(3, "What does a circular sign with a red border mean?", 
            listOf("Warning", "Information", "Prohibition", "Mandatory"), 2, categories[0]),
        Question(4, "What does a blue rectangular sign indicate?", 
            listOf("Warning", "Information", "Prohibition", "Mandatory"), 1, categories[0]),
        Question(5, "What does a triangular sign with red border mean?", 
            listOf("Stop", "Yield", "Warning", "No Entry"), 1, categories[0]),
        
        // Road Rules
        Question(6, "What is the maximum speed limit in residential areas?", 
            listOf("30 km/h", "40 km/h", "50 km/h", "60 km/h"), 2, categories[1]),
        Question(7, "When should you use your turn signal?", 
            listOf("Only when turning left", "Only when turning right", "Before any turn or lane change", "Only on highways"), 2, categories[1]),
        Question(8, "What should you do at a yellow traffic light?", 
            listOf("Speed up", "Stop if safe to do so", "Continue normally", "Honk your horn"), 1, categories[1]),
        Question(9, "How far should you follow behind another vehicle?", 
            listOf("1 second", "2 seconds", "3 seconds", "5 seconds"), 2, categories[1]),
        Question(10, "When is it legal to pass another vehicle?", 
            listOf("Anytime", "Only on highways", "When safe and legal", "Never"), 2, categories[1]),
        
        // Safety
        Question(11, "What should you do if your brakes fail?", 
            listOf("Pump the brakes", "Use parking brake gradually", "Turn off engine", "All of the above"), 3, categories[2]),
        Question(12, "When should you check your mirrors?", 
            listOf("Only when changing lanes", "Every 5-8 seconds", "Only when parking", "Once per trip"), 1, categories[2]),
        Question(13, "What is the safest way to handle a tire blowout?", 
            listOf("Brake hard immediately", "Grip wheel firmly and slow down gradually", "Turn sharply", "Accelerate"), 1, categories[2]),
        Question(14, "What should you do in heavy rain?", 
            listOf("Drive faster", "Use hazard lights", "Reduce speed and increase following distance", "Drive normally"), 2, categories[2]),
        Question(15, "When should you use headlights?", 
            listOf("Only at night", "30 minutes before sunset to 30 minutes after sunrise", "Only in bad weather", "Never during day"), 1, categories[2]),
        
        // Parking
        Question(16, "How far from a fire hydrant should you park?", 
            listOf("3 meters", "5 meters", "10 meters", "15 meters"), 1, categories[3]),
        Question(17, "When parking uphill with a curb, which way should you turn your wheels?", 
            listOf("Away from curb", "Toward curb", "Straight", "Either way"), 0, categories[3]),
        Question(18, "How close to a crosswalk can you park?", 
            listOf("Right up to it", "3 meters", "5 meters", "10 meters"), 2, categories[3]),
        Question(19, "What is parallel parking?", 
            listOf("Parking perpendicular to curb", "Parking parallel to curb", "Parking at an angle", "Parking in a garage"), 1, categories[3]),
        Question(20, "When parking downhill, which way should you turn your wheels?", 
            listOf("Away from curb", "Toward curb", "Straight", "Either way"), 1, categories[3]),
        
        // Intersections
        Question(21, "Who has the right of way at a four-way stop?", 
            listOf("Largest vehicle", "First to arrive", "Vehicle on the right", "Vehicle going straight"), 1, categories[4]),
        Question(22, "What should you do when approaching a flashing red light?", 
            listOf("Slow down", "Stop completely", "Proceed with caution", "Speed up"), 1, categories[4]),
        Question(23, "When turning left at an intersection, you should yield to:", 
            listOf("No one", "Oncoming traffic", "Pedestrians only", "Vehicles behind you"), 1, categories[4]),
        Question(24, "What does a flashing yellow light mean?", 
            listOf("Stop", "Yield", "Proceed with caution", "Speed up"), 2, categories[4]),
        Question(25, "At a roundabout, who has the right of way?", 
            listOf("Entering traffic", "Traffic in the roundabout", "Largest vehicle", "Emergency vehicles only"), 1, categories[4]),
        
        // Highway Driving
        Question(26, "What is the minimum speed on most highways?", 
            listOf("40 km/h", "60 km/h", "80 km/h", "100 km/h"), 1, categories[5]),
        Question(27, "When merging onto a highway, you should:", 
            listOf("Stop and wait", "Match the speed of traffic", "Go as fast as possible", "Use hazard lights"), 1, categories[5]),
        Question(28, "What lane should you use for normal highway driving?", 
            listOf("Left lane", "Right lane", "Middle lane", "Any lane"), 1, categories[5]),
        Question(29, "When should you use the left lane on a highway?", 
            listOf("Normal driving", "Passing only", "Slow driving", "Emergency only"), 1, categories[5]),
        Question(30, "What should you do if you miss your highway exit?", 
            listOf("Reverse", "Stop and back up", "Continue to next exit", "Make a U-turn"), 2, categories[5])
    )
    
    override suspend fun getCategories(): List<Category> {
        return categories
    }
    
    override suspend fun getQuestionsByCategory(categoryId: Int, limit: Int): List<Question> {
        return questions.filter { it.category.id == categoryId }.shuffled().take(limit)
    }
    
    override suspend fun getAllQuestions(): List<Question> {
        return questions
    }
}