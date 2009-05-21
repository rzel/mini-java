package mini.java.fa.v1.adapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class used to convert Object inputs to Character inputs and vice versa.
 *
 * @author Alex
 */
public class InputConvertor {
    // mapping from NFA input to SimpleFA input
    private Map<Object, Character> _objectToCharacter;

    // mapping from SimpleFA input to NFA input
    private Map<Character, Object> _characterToObject;

    // used to track used characters by simpleFA
    private char                   _charValue;
    
    public InputConvertor() {
        _objectToCharacter  = new HashMap<Object, Character>();
        _characterToObject  = new HashMap<Character, Object>();
        _charValue          = 'A'; // start from 'A' for debug output
    }
    
    /**
     * Converts an object input to a character input. If there is no
     * such mapping for the given object input, create a new character
     * input for it.
     */
    public Character convert(Object input_) {
        if (!_objectToCharacter.containsKey(input_)) {
            Character c = new Character(_charValue++);
            _objectToCharacter.put(input_, c);
            _characterToObject.put(c, input_);
        }
        
        return _objectToCharacter.get(input_);
    }
    
    /**
     * Converts a character input to an object input. If there is no
     * such mapping for the given character input, returns null.
     */
    public Object convert(Character input_) {
        return _characterToObject.get(input_);
    }
}
