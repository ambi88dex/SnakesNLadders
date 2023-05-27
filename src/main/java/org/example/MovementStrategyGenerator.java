package org.example;

public class MovementStrategyGenerator {

    MovementStrategy strategy;

    MovementStrategyGenerator(MovementStrategy _strategy){
        strategy = _strategy;
    }

    private int getSingleDieValue(){
        return 1 + (int) Math.floor(Math.random()*6);
    }

    static boolean strategyLogsShown;

    static {
        strategyLogsShown = false;
    }

    public int throwDies(int numberOfDies){

        // this design avoids use of memory to calculate throw of dies.

        int currentSpellValue = getSingleDieValue();
        if(!strategyLogsShown){
            System.out.println("strategyLogShown -> spin = " + currentSpellValue);
        }

        for(int i = 1; i < numberOfDies; i++){
            int newSpellValue = getSingleDieValue();
            switch (strategy){
                case SUM -> {
                    currentSpellValue += newSpellValue;
                    if(!strategyLogsShown){
                        System.out.println("strategyLogShown -> SUM spin = " + currentSpellValue);
                    }
                }
                case MAX -> {
                    currentSpellValue = Math.max(currentSpellValue, newSpellValue);
                    if(!strategyLogsShown){
                        System.out.println("strategyLogShown -> MAX spin = " + currentSpellValue);
                    }
                }
                case MIN -> {
                    currentSpellValue = Math.min(currentSpellValue, newSpellValue);
                    if(!strategyLogsShown){
                        System.out.println("strategyLogShown -> MIN spin = " + currentSpellValue);
                    }
                }
            }
        }
        if(!strategyLogsShown){
            strategyLogsShown = true;
        }

        return currentSpellValue;

    }
}
