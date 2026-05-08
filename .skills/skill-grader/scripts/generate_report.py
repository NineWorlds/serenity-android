import json
import os

def generate_report(target_skill_path):
    compliance_report_path = os.path.join(target_skill_path, 'memory', 'compliance_report.json')
    template_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', 'assets', 'audit_report_template.html')

    with open(compliance_report_path, 'r') as f:
        data = json.load(f)

    with open(template_path, 'r') as f:
        template = f.read()

    # Manual rendering logic since jinja2 is not available
    findings_rows = ""
    for category, detail in data['findings'].items():
        formatted_category = category.replace('_', ' ').title()
        findings_rows += f"        <tr><td>{formatted_category}</td><td>{detail['score']}%</td><td>{detail['text']}</td></tr>\n"

    suggestions_list = ""
    for suggestion in data['suggestions']:
        suggestions_list += f"            <li>{suggestion}</li>\n"

    html_content = template.replace('{{skill_name}}', data['skill_name']) \
                           .replace('{{grade}}', data['grade']) \
                           .replace('{{score}}', str(data['score'])) \
                           .replace('{{audit_date}}', data['audit_date']) \
                           .replace('{% for category, data in findings.items() %}\n        <tr><td>{{ category|capitalize|replace(\'_\', \' \') }}</td><td>{{ data.score }}%</td><td>{{ data.text }}</td></tr>\n        {% endfor %}', findings_rows) \
                           .replace('            {% for suggestion in suggestions %}\n            <li>{{ suggestion }}</li>\n            {% endfor %}', suggestions_list)

    output_path = os.path.join(target_skill_path, 'memory', 'audit_report.html')
    with open(output_path, 'w') as f:
        f.write(html_content)
    
    print(f"Report generated at {output_path}")

if __name__ == "__main__":
    import sys
    if len(sys.argv) > 1:
        generate_report(sys.argv[1])
